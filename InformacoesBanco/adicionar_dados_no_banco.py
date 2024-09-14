import mysql.connector
import uuid
import json
import re
from dotenv import load_dotenv
import os
from datetime import datetime

# Carregar o arquivo .env
load_dotenv()

# Acessar as variáveis de ambiente
jdbc_url = os.getenv("JDBC_URL")
jdbc_user = os.getenv("JDBC_USER")
jdbc_password = os.getenv("JDBC_PASSWORD")

# Conectar ao banco de dados MySQL
def connect_db():
    return mysql.connector.connect(
        host="localhost",
        user=jdbc_user,  # usuário
        password=jdbc_password,  # senha
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

def format_date(date_str):
    if not date_str:
        return None
    try:
        # Remover espaços em branco e corrigir possíveis erros de formatação
        date_str = date_str.strip()

        # Corrigir entradas no formato 'DD/MMYYYY' (sem segundo '/')
        if re.match(r'^\d{2}/\d{2}\d{4}$', date_str):
            date_str = date_str[:5] + '/' + date_str[5:]  # Adiciona a barra no lugar correto

        # Tenta formatar a data no formato 'DDMMYYYY' ou 'DD/MM/YYYY'
        if re.match(r'^\d{8}$', date_str):  # Caso no formato 'DDMMYYYY'
            return datetime.strptime(date_str, '%d%m%Y').strftime('%Y-%m-%d')
        elif re.match(r'^\d{2}/\d{2}/\d{4}$', date_str):  # Caso no formato 'DD/MM/YYYY'
            return datetime.strptime(date_str, '%d/%m/%Y').strftime('%Y-%m-%d')
        else:
            return date_str  # Retorna a data como está, se já estiver no formato 'YYYY-MM-DD'
    except ValueError:
        print(f"Erro ao formatar a data: {date_str}")
        return None

def insert_document(cursor, project_id, file_type, file_url):
    cursor.execute(
        "INSERT INTO documents (documents_id, file_name, file_type, file_url, project_id) VALUES (%s, %s, %s, %s, %s)",
        (str(uuid.uuid4()), file_url.split("/")[-1], file_type, file_url, project_id)
    )

def insert_projeto(cursor, data):
    projeto_id = str(uuid.uuid4())
    coordenador_nome = data.get("Coordenador", "SEM COORDENADOR")

    # Usa .get() para valores com chave ausente
    valor_projeto = data.get("Valor do projeto", "")
    data_inicio = format_date(data.get("Data de início", ""))
    data_termino = format_date(data.get("Data de término", ""))

    cursor.execute(
        "INSERT INTO projects (project_id, name_coordinator, project_company, project_description, project_end_date, project_objective, project_reference, project_start_date, project_value) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)",
        (
            projeto_id,
            coordenador_nome,
            data.get("Empresa", ""),
            data.get("Descrição", ""),
            data_termino,
            data.get("Objeto", ""),
            data.get("Referência do projeto", ""),
            data_inicio,
            format_valor(valor_projeto)
        )
    )

    for url in data.get("Contratos", []):
        insert_document(cursor, projeto_id, "CONTRATO", url)

    for url in data.get("Propostas", []):
        insert_document(cursor, projeto_id, "PROPOSTA", url)

    for url in data.get("Artigos", []):
        insert_document(cursor, projeto_id, "ARTIGO", url)

def process_json_file(file_path):
    # Abrindo o arquivo JSON com codificação UTF-8
    with open(file_path, 'r', encoding='utf-8') as file:
        data_list = json.load(file)

    db = connect_db()
    cursor = db.cursor()
    try:
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
