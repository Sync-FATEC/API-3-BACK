package com.sync.api.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class RunPythonScript {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/api202402";
        String jdbcUser = "root";
        String jdbcPassword = "root";
        String tableName = "projetos";
        String pythonInterpreter = "python3"; // Ou "python" dependendo do seu sistema
        String scriptPath = "informacoesBanco/adicionar_dados_no_banco.py";

        try {
            // Conectar ao banco de dados
            Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
            Statement statement = connection.createStatement();

            // Verificar se a tabela está vazia
            String query = "SELECT COUNT(*) FROM " + tableName;
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                if (count == 0) {
                    System.out.println("O banco de dados está vazio. Executando o script Python...");

                    // Executar o script Python
                    ProcessBuilder pb = new ProcessBuilder(pythonInterpreter, scriptPath);
                    Process process = pb.start();

                    // Ler a saída do processo
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }

                    // Esperar o processo terminar e verificar o código de saída
                    int exitCode = process.waitFor();
                    System.out.println("Código de saída: " + exitCode);
                } else {
                    System.out.println("O banco de dados não está vazio.");
                }
            }

            // Fechar recursos
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
