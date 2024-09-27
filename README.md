<h1>Repositório Backend</h1>
<h2>🛠️ Execução do sistema</h2>
  <h3>Passo 1: Instale as Dependências</h3>
  <p>Certifique-se de ter o Java e o Spring instalado no seu sistema.</p>
  <p>Para baixar o Java <a href="https://www.oracle.com/java/technologies/downloads/#java11-linux">https://www.oracle.com/java/</a>.</p>
  <p>Para baixar o Spring <a href="https://start.spring.io/">https://start.spring.io/</a>.</p>
  <h3>Passo 2: Abra o Terminal</h3>
  <p>Pesquise por "Terminal" na barra de tarefas do seu dispositivo e abra o terminal.</p>
  <h3>Passo 3: Clone o Repositório</h3>
  <pre><code>git clone https://github.com/Sync-FATEC/API-3-BACK</code></pre>

  <h3>Passo 4: Configure o Banco de Dados</h3>
  <p>Abra o arquivo ".env" e altere as credenciais do banco de dados conforme necessário:</p>
  <pre><code>
  DBC_URL=jdbc:mysql://localhost:3306/
  JDBC_USER=seu_usuario
  JDBC_PASSWORD=sua_senha
  </code></pre>

  <p>Abra o arquivo "application.properties" e altere as credenciais do banco de dados conforme necessário:</p>
  <pre><code>
  spring.datasource.url=jdbc:mysql://localhost:3306/api202402
  spring.datasource.username=seu_usuario
  spring.datasource.password=sua_senha
  </code></pre>

  <h3>Passo 5: Inicie a Aplicação</h3>
  <p>Para iniciar a aplicação, execute o comando:</p>
  <pre><code>java -jar caminhoarquivo.jar</code></pre>
