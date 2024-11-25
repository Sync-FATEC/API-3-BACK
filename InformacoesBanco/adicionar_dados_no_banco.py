import mysql.connector
import uuid
import json
import re
from dotenv import load_dotenv
import os
from datetime import datetime

# Load file .env
load_dotenv()

# Access environment variables
jdbc_url = os.getenv("JDBC_URL")
jdbc_user = os.getenv("JDBC_USER")
jdbc_password = os.getenv("JDBC_PASSWORD")

def connect_db():
    """ Connect to MySQL database """
    return mysql.connector.connect(
        host="localhost",
        user=jdbc_user,  # user
        password=jdbc_password,  # password
        database="api202402"  # database
    )

def forma_value(value):
    """ Format the value to float """
    if not value:
        return 0.0
    value = re.sub(r'[^\d,.]', '', value)  # Remove non-numeric characters
    value = value.replace(".", "")  # Remove periods that are not decimal
    return float(value.replace(",", "."))

def format_date(date_str):
    """ Format the date to 'DD/MM/YYYY' """
    if not date_str:
        return None
    try:
        date_str = date_str.strip()
        if re.match(r'^\d{2}/\d{2}\d{4}$', date_str):
            date_str = date_str[:5] + '/' + date_str[5:]  # Add '/' between day and month
        if re.match(r'^\d{8}$', date_str):
            return datetime.strptime(date_str, '%d%m%Y').strftime('%Y-%m-%d')
        elif re.match(r'^\d{2}/\d{2}/\d{4}$', date_str):
            return datetime.strptime(date_str, '%d/%m/%Y').strftime('%Y-%m-%d')
        else:
            return date_str
    except ValueError:
        print(f"Erro ao formatar a data: {date_str}")
        return None

def normalize_coordinator_name(name):
    """ Normalize coordinator name by removing titles and standardizing variations """
    name = name.strip().lower()
    name = re.sub(r'\b(prof|dr|msc|phd|eng)\.', '', name, flags=re.IGNORECASE)
    name = name.replace(".", "")
    name = re.sub(r'\s+', ' ', name).strip()
    return name

def insert_coordinator(cursor, coordinator_name, seen_coordinators):
    normalized_name = normalize_coordinator_name(coordinator_name)

    if normalized_name in seen_coordinators:
        return seen_coordinators[normalized_name]  # Return existing ID if already in the dictionary

    coordinator_id = str(uuid.uuid4())
    cursor.execute(
        "INSERT INTO coordinators (coordinator_id, coordinator_name) VALUES (%s, %s)",
        (coordinator_id, coordinator_name)
    )
    seen_coordinators[normalized_name] = coordinator_id  # Store in dictionary
    return coordinator_id

def normalize_company_name(company_name):
    """ Normalize company name by removing abbreviations, extra spaces, and standardizing variations """
    company_name = company_name.strip().lower()
    company_name = re.sub(r'\b(s\.a\.|ltda\.?|eireli)\b', '', company_name, flags=re.IGNORECASE)
    company_name = re.sub(r'\s+', ' ', company_name).strip()
    return company_name

def insert_document(cursor, project_id, file_url):
    file_type = None
    if 'trabalho' in file_url.lower():
        file_type = 'PLANO_DE_TRABALHO'
    elif 'aditivo' in file_url.lower():
        file_type = 'TERMO_ADITIVO'
    elif 'contrato' in file_url.lower():
        file_type = 'CONTRATO'
    else:
        file_type = 'OUTROS'

    cursor.execute(
        "INSERT INTO documents (documents_id, file_name, file_type, file_url, project_id) VALUES (%s, %s, %s, %s, %s)",
        (str(uuid.uuid4()), file_url.split("/")[-1], file_type, file_url, project_id)
    )

def insert_company(cursor, company_name, seen_companies):
    """ Insert company into the company table and return its ID """
    normalized_company_name = normalize_company_name(company_name)

    # Verifica se a empresa já está no dicionário de empresas vistas
    if normalized_company_name in seen_companies:
        return seen_companies[normalized_company_name]  # Retorna o ID já armazenado

    # Gera um novo UUID para a empresa
    company_id = str(uuid.uuid4())

    # Insere a empresa na tabela company
    cursor.execute(
        "INSERT INTO company (id, corporate_name, private_company) VALUES (%s, %s, %s)",
        (company_id, company_name, 0)
    )

    # Armazena o ID da empresa no dicionário para evitar inserções duplicadas
    seen_companies[normalized_company_name] = company_id
    return company_id

def insert_projeto(cursor, data, seen_coordinators, seen_companies):
    project_id = str(uuid.uuid4())
    coordinator_name = data.get("Coordenador", "SEM COORDENADOR")
    company_name = data.get("Empresa", "")

    # Insere o coordenador e obtém seu ID
    coordinator_id = insert_coordinator(cursor, coordinator_name, seen_coordinators)

    # Insere a empresa e obtém o ID da empresa
    company_id = insert_company(cursor, company_name, seen_companies)

    value_project = data.get("Valor do projeto", "")
    data_inicio = format_date(data.get("Data de início", ""))
    data_termino = format_date(data.get("Data de término", ""))

    # Verifica se o campo "Objeto" está presente, caso contrário, define uma mensagem padrão
    project_objective = data.get("Objeto")
    if project_objective == "":
        project_objective = "Sem objeto definido"

    project_description = data.get("Descrição", "")

    if project_description == "":
        project_description = "Sem descrição definida"

    # Insere o projeto na tabela projects
    cursor.execute(
        "INSERT INTO projects (project_id, coordinator_id, company_id, project_description, project_end_date, project_objective, project_reference, project_title, project_start_date, project_value, project_classification, project_status, is_draft) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)",
        (
            project_id,
            coordinator_id,
            company_id,  # Usa o ID da empresa em vez do nome
            project_description,
            data_termino,
            project_objective,
            data.get("Referência do projeto", ""),
            data.get("Titulo do projeto", ""),
            data_inicio,
            forma_value(value_project),
            "CONTRATOS",
            "FINALIZADOS",
            0
        )
    )

    # Insere os documentos
    for url in data.get("Contratos", []):
        insert_document(cursor, project_id, url)

    for url in data.get("Propostas", []):
        insert_document(cursor, project_id, url)

    for url in data.get("Artigos", []):
        insert_document(cursor, project_id, url)

def process_json_file(file_path):
    """ Process the json file and insert data into the database """
    with open(file_path, 'r', encoding='utf-8') as file:
        data_list = json.load(file)

    db = connect_db()
    cursor = db.cursor()
    seen_coordinators = {}
    seen_companies = {}
    try:
        for data in data_list:
            insert_projeto(cursor, data, seen_coordinators, seen_companies)
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
    file_path = 'InformacoesBanco/data_project.json'
    process_json_file(file_path)

if __name__ == "__main__":
    main()
