import requests
from bs4 import BeautifulSoup
import json
from urllib.parse import urljoin

def raspar_informacoes_projeto(url):
    # Enviar a requisição para a URL
    resposta = requests.get(url)
    resposta.raise_for_status()  # Levanta um erro se a requisição falhar

    # Parsear o conteúdo HTML da página
    sopa = BeautifulSoup(resposta.text, 'html.parser')

    # Dicionário para armazenar as informações
    info_projeto = {}

    # Lista com os títulos esperados
    titulos_esperados = [
        'Referência do projeto',
        'Empresa',
        'Objeto',
        'Descrição',
        'Coordenador',
        'Valor do projeto',
        'Data de início',
        'Data de término'
    ]

    # Extraindo as informações dos divs com a classe 'row'
    for linha in sopa.find_all('div', class_='row'):
        titulo_div = linha.find('div', class_='col-sm-3')
        valor_div = linha.find('div', class_='col-sm-9')
        
        if titulo_div and valor_div:
            titulo = titulo_div.get_text(strip=True)
            valor = valor_div.get_text(strip=True)

            if titulo in titulos_esperados:
                info_projeto[titulo] = valor

    # Base URL para construir URLs absolutas
    url_base = 'https://fapg.org.br/'

    # Coletando URLs relevantes
    urls_propostas = []
    urls_contratos = []
    urls_artigos = []

    for link in sopa.find_all('a', href=True):
        texto_link = link.get_text(strip=True)
        url_completa = urljoin(url_base, link['href'])

        if 'Proposta' in texto_link or 'Relatório Técnico' in texto_link:
            urls_propostas.append(url_completa)
        elif 'Contrato' in texto_link:
            urls_contratos.append(url_completa)
        elif 'Artigo' in texto_link:
            urls_artigos.append(url_completa)

    # Adicionando URLs ao dicionário
    info_projeto['Propostas'] = urls_propostas
    info_projeto['Contratos'] = urls_contratos
    info_projeto['Artigos'] = urls_artigos

    return info_projeto

# URL da página principal
url_base = 'https://fapg.org.br/projetos/index.php?act=filter&projeto=&coordenador=&inicio=&termino=&classificacao=&filter=all'

# Fazendo a requisição para a página principal
resposta = requests.get(url_base)
sopa = BeautifulSoup(resposta.text, 'html.parser')

# Extraindo links dos projetos
dados_projetos = []
for linha in sopa.find_all('tr'):
    link = linha.find('a', href=True)
    if link:
        url_detalhe = urljoin(url_base, link['href'])
        dados_projetos.append(raspar_informacoes_projeto(url_detalhe))

# Salvando os dados em um arquivo JSON
with open('dados_projetos.json', 'w', encoding='utf-8') as arquivo_json:
    json.dump(dados_projetos, arquivo_json, indent=4, ensure_ascii=False)

print("Dados dos projetos salvos em dados_projetos.json")
