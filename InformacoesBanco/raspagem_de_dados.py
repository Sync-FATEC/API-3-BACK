################################################################################
# Imports

import requests
from bs4 import BeautifulSoup
import json
from urllib.parse import urljoin

################################################################################
def scrape_project_information(url):
    # Send the request to the URL
    response = requests.get(url)
    response.raise_for_status()  # Raise an exception for 4xx and 5xx status codes

    # Parse the HTML content
    sopa = BeautifulSoup(response.text, 'html.parser')

    # Dictionary to store the information of the project
    info_project = {}

    # List with the expected titles
    titles_expected = [
        'Referência do projeto',
        'Empresa',
        'Objeto',
        'Descrição',
        'Coordenador',
        'Valor do projeto',
        'Data de início',
        'Data de término'
    ]

    # Extrain information from the project
    for row in sopa.find_all('div', class_='row'):
        title_div = row.find('div', class_='col-sm-3')
        value_div = row.find('div', class_='col-sm-9')
        
        if title_div and value_div:
            title = title_div.get_text(strip=True)
            value = value_div.get_text(strip=True)

            if title in titles_expected:
                info_project[title] = value

    # URL of the project
    url_base = 'https://fapg.org.br/'

    # Get the project name
    urls_proposals = []
    urls_contracts = []
    urls_articles = []

    for link in sopa.find_all('a', href=True):
        text_link = link.get_text(strip=True)
        url_complete = urljoin(url_base, link['href'])

        if 'Proposta' in text_link or 'Relatório Técnico' in text_link:
            urls_proposals.append(url_complete)
        elif 'Contrato' in text_link:
            urls_contracts.append(url_complete)
        elif 'Artigo' in text_link:
            urls_articles.append(url_complete)

    # Adding URLs to the dictionary    
    info_project['Propostas'] = urls_proposals
    info_project['Contratos'] = urls_contracts
    info_project['Artigos'] = urls_articles

    reference_number = ''
    title_project = ''

    for index, item in enumerate(info_project['Referência do projeto']):
        if item not in '0123456789' and index == 0:
            title_project = info_project['Referência do projeto']
            break
        elif item in '0123456789-/':
            reference_number += item
        elif item == " ":
            title_project = info_project['Referência do projeto'][index+1:]
            break

    if reference_number == '':
        reference_number = 'Sem referência'

    if reference_number[-1] == '-':
        reference_number = reference_number[:-1]

    info_project['Referência do projeto'] = reference_number
    info_project['Titulo do projeto'] = title_project

    if info_project['Titulo do projeto'][0] == '-':
        info_project['Titulo do projeto'] = info_project['Titulo do projeto'][2:]

    return info_project

################################################################################

if __name__ == '__main__':
    # URL of principal page
    url_base = 'https://fapg.org.br/projetos/index.php?act=filter&projeto=&coordenador=&inicio=&termino=&classificacao=&filter=all'

    # Make the request to the principal page
    response = requests.get(url_base)
    sopa = BeautifulSoup(response.text, 'html.parser')

    # Extrain information from the projects
    data_project = []
    for row in sopa.find_all('tr'):
        link = row.find('a', href=True)
        if link:
            url_detail = urljoin(url_base, link['href'])
            data_project.append(scrape_project_information(url_detail))

    # Saving the data in a JSON file
    with open('data_project.json', 'w', encoding='utf-8') as arquivo_json:
        json.dump(data_project, arquivo_json, indent=4, ensure_ascii=False)

    print("Data of the projects saved in data_project.json")