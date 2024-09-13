import mysql.connector
import uuid
import json
import re
from datetime import datetime

# Conectar ao banco de dados MySQL
def connect_db():
    return mysql.connector.connect(
        host="localhost",
        user="root",  # usuário
        password="root",  # senha
        database="api202402"  # banco de dados
    )

def format_valor(valor):
    if not valor:
        return 0.0
    # Remove qualquer caractere não numérico, exceto o ponto e a vírgula
    valor = re.sub(r'[^\d,.]', '', valor)
    # Remove o ponto usado como separador de milhar
    valor = valor.replace(".", "")
    # Substitui a vírgula por ponto
    return float(valor.replace(",", "."))

def get_or_create_coordenador(cursor, nome):
    cursor.execute("SELECT id FROM coordenadores WHERE nome = %s", (nome,))
    result = cursor.fetchone()
    if result:
        return result[0]
    else:
        new_id = str(uuid.uuid4())
        cursor.execute(
            "INSERT INTO coordenadores (id, nome) VALUES (%s, %s)",
            (new_id, nome)
        )
        return new_id

def ensure_sem_coordenador(cursor):
    # Garante que o coordenador "SEM COORDENADOR" existe
    get_or_create_coordenador(cursor, "SEM COORDENADOR")

def insert_anexos(cursor, projeto_id, tipo, url):
    cursor.execute(
        "INSERT INTO anexos (id, nome, tipo, url, projeto_id) VALUES (%s, %s, %s, %s, %s)",
        (str(uuid.uuid4()), "", tipo, url, projeto_id)
    )

def insert_projeto(cursor, data):
    projeto_id = str(uuid.uuid4())
    coordenador_nome = data.get("Coordenador", "SEM COORDENADOR")
    coordenador_id = get_or_create_coordenador(cursor, coordenador_nome)
    
    # Usa .get() para valores com chave ausente
    valor_projeto = data.get("Valor do projeto", "")
    
    cursor.execute(
        "INSERT INTO projetos (id, classificacao, data_inicio, data_termino, descricao, empresa, objetivo, referencia_do_projeto, situacao, valor_do_projeto, coordenador_id) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)",
        (
            projeto_id,
            "CONTRATOS",  # Corrigido para um valor válido no ENUM
            data.get("Data de início", ""),
            data.get("Data de término", ""),
            data.get("Descrição", ""),
            data.get("Empresa", ""),
            data.get("Objeto", ""),
            data.get("Referência do projeto", ""),
            "FINALIZADOS",
            format_valor(valor_projeto),
            coordenador_id
        )
    )
    
    for url in data.get("Contratos", []):
        insert_anexos(cursor, projeto_id, "CONTRATO", url)

    for url in data.get("Propostas", []):
        insert_anexos(cursor, projeto_id, "PROPOSTA", url)

    for url in data.get("Artigos", []):
        insert_anexos(cursor, projeto_id, "ARTIGO", url)

def process_json_file(file_path):
    # Abrindo o arquivo JSON com codificação UTF-8
    with open(file_path, 'r', encoding='utf-8') as file:
        data_list = json.load(file)
        
    db = connect_db()
    cursor = db.cursor()
    try:
        ensure_sem_coordenador(cursor)
        for data in data_list:
            insert_projeto(cursor, data)
        db.commit()
    except mysql.connector.Error as err:
        print(f"Error: {err}")
        db.rollback()
    except ValueError as ve:
        print(f"ValueError: {ve}")
    finally:
        cursor.close()
        db.close()

def main():
    file_path = 'informacoesBanco/dados_projetos.json'
    process_json_file(file_path)

if __name__ == "__main__":
    main()
