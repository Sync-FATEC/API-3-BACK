################################################################################
# Imports 

import mysql.connector
import uuid
import json
import re
from dotenv import load_dotenv
import os
from datetime import datetime

################################################################################

# Load file .env
load_dotenv()

# Access environment variables
jdbc_url = os.getenv("JDBC_URL")
jdbc_user = os.getenv("JDBC_USER")
jdbc_password = os.getenv("JDBC_PASSWORD")

################################################################################
def connect_db():
    """ Connect to MySQL database """
    
    return mysql.connector.connect(
        host="localhost",
        user=jdbc_user,  # user
        password=jdbc_password,  # password
        database="api202402"  # database
    )

################################################################################
def forma_value(value):
    """ Format the value to float """
    
    if not value:
        return 0.0
    # Remove characters except number, comma and period
    value = re.sub(r'[^\d,.]', '', value)
    # Remove periods that are not decimal separator
    value = value.replace(".", "")
    # Replace comma with period
    return float(value.replace(",", "."))

################################################################################
def format_date(date_str):
    """ Format the date to 'DD/MM/YYYY' """
    
    if not date_str:
        return None
    try:
        # Remove spaces from the beginning and end of the string
        date_str = date_str.strip()

        # To correct date to format 'DD/MMYYYY' or 'DD/MM/YYYY'
        if re.match(r'^\d{2}/\d{2}\d{4}$', date_str):
            date_str = date_str[:5] + '/' + date_str[5:]  # Add '/' between day and month

        # try to parse the date in different formats
        if re.match(r'^\d{8}$', date_str):  # Case in the format 'DDMMYYYY'
            return datetime.strptime(date_str, '%d%m%Y').strftime('%Y-%m-%d')
        elif re.match(r'^\d{2}/\d{2}/\d{4}$', date_str):  # Case in the format 'DD/MM/YYYY'
            return datetime.strptime(date_str, '%d/%m/%Y').strftime('%Y-%m-%d')
        else:
            return date_str  # Return the date as it is if it is not in the expected format
    
    except ValueError:
        print(f"Erro ao formatar a data: {date_str}")
        return None
    
################################################################################
def handle_file_type(file_name):
    if "contrato" in file_name.lower():
        return "CONTRATO"
    elif "proposta" in file_name.lower() or "trabalho" in file_name.lower():
        return "PROPOSTA"
    elif "artigo" in file_name.lower():
        return "ARTIGO"


################################################################################
def insert_document(cursor, project_id, file_url):
    cursor.execute(
        "INSERT INTO documents (documents_id, file_name, file_type, file_url, project_id) VALUES (%s, %s, %s, %s, %s)",
        (str(uuid.uuid4()), file_url.split("/")[-1], handle_file_type(file_url.split("/")[-1]), file_url, project_id)
    )

################################################################################
def insert_projeto(cursor, data):
    project_id = str(uuid.uuid4())
    coordinator_name = data.get("Coordenador", "SEM COORDENADOR")

    # Use .get() for values with missing key
    value_project = data.get("Valor do projeto", "")
    data_inicio = format_date(data.get("Data de início", ""))
    data_termino = format_date(data.get("Data de término", ""))

    cursor.execute(
        "INSERT INTO projects (project_id, name_coordinator, project_company, project_description, project_end_date, project_objective, project_reference, project_start_date, project_value, project_classification, project_status) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)",
        (
            project_id,
            coordinator_name,
            data.get("Empresa", ""),
            data.get("Descrição", ""),
            data_termino,
            data.get("Objeto", ""),
            data.get("Referência do projeto", ""),
            data_inicio,
            forma_value(value_project),
            "CONTRATOS",
            "FINALIZADOS"
        )
    )

    for url in data.get("Contratos", []):
        insert_document(cursor, project_id, url)

    for url in data.get("Propostas", []):
        insert_document(cursor, project_id, url)

    for url in data.get("Artigos", []):
        insert_document(cursor, project_id, url)

################################################################################
def process_json_file(file_path):
    """ Process the json file and insert data into the database """
    
    # Open json file and load data
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

################################################################################
def main():
    file_path = 'informacoesBanco/data_project.json'
    process_json_file(file_path)

################################################################################
if __name__ == "__main__":
    main()