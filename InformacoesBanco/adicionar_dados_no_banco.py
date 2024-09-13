import mysql.connector
import uuid
import json
import re
from dotenv import load_dotenv
import os

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

    cursor.execute(
        "INSERT INTO projects (project_id, cpf_coordinator, name_coordinator, project_company, project_description, project_end_date, project_objective, project_reference, project_start_date, project_value) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s)",
        (
            projeto_id,
            None,  # cpf_coordinator não está presente no JSON
            coordenador_nome,
            data.get("Empresa", ""),
            data.get("Descrição", ""),
            data.get("Data de término", ""),
            data.get("Objeto", ""),
            data.get("Referência do projeto", ""),
            data.get("Data de início", ""),
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
