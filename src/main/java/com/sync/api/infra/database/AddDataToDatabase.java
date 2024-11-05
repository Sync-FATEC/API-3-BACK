package com.sync.api.infra.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;

public class AddDataToDatabase {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/api202402";
        String jdbcUser = Definitions.JDBC_USER;
        String jdbcPassword = Definitions.JDBC_PASSWORD;
        String tableName = "projects";
        String pythonInterpreter = "python"; // Use "python3" if necessary
        String scriptPath = "InformacoesBanco" + File.separator + "adicionar_dados_no_banco.py";
        String requirementsPath = "InformacoesBanco" + File.separator + "requirements.txt";
        String venvPath = "InformacoesBanco" + File.separator + "venv";

        try {
            // Connecting to the database
            Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
            Statement statement = connection.createStatement();

            // Check if the table is empty
            String query = "SELECT COUNT(*) FROM " + tableName;
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                int count = resultSet.getInt(1);

                if (count == 0) {
                    // Create a virtual environment
                    ProcessBuilder venvCreate = new ProcessBuilder(pythonInterpreter, "-m", "venv", venvPath);
                    Process venvProcess = venvCreate.start();
                    venvProcess.waitFor();
                    System.out.println("Virtual environment created.");

                    // Install Python dependencies
                    String venvPip = venvPath + File.separator + "Scripts" + File.separator + "pip.exe"; // Windows path
                    if (!System.getProperty("os.name").toLowerCase().contains("win")) {
                        venvPip = venvPath + File.separator + "bin" + File.separator + "pip"; // Unix-based path
                    }

                    ProcessBuilder pipInstall = new ProcessBuilder(venvPip, "install", "-r", requirementsPath);
                    Process pipProcess = pipInstall.start();

                    try (BufferedReader pipStdInput = new BufferedReader(new InputStreamReader(pipProcess.getInputStream()));
                         BufferedReader pipStdError = new BufferedReader(new InputStreamReader(pipProcess.getErrorStream()))) {

                        String pipLine;
                        System.out.println("Pip install output:");
                        while ((pipLine = pipStdInput.readLine()) != null) {
                            System.out.println(pipLine);
                        }

                        System.out.println("Pip install errors (if any):");
                        while ((pipLine = pipStdError.readLine()) != null) {
                            System.out.println(pipLine);
                        }

                        int pipExitCode = pipProcess.waitFor();
                        System.out.println("Pip install exit code: " + pipExitCode);

                        if (pipExitCode != 0) {
                            System.out.println("Error during pip install. Exit code: " + pipExitCode);
                        }
                    }

                    // Run the Python script
                    String venvPython = venvPath + File.separator + "Scripts" + File.separator + "python.exe"; // Windows
                    if (!System.getProperty("os.name").toLowerCase().contains("win")) {
                        venvPython = venvPath + File.separator + "bin" + File.separator + "python"; // Unix-based
                    }

                    ProcessBuilder pb = new ProcessBuilder(venvPython, scriptPath);
                    Process process = pb.start();

                    try (BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
                         BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {

                        String line;
                        while ((line = stdInput.readLine()) != null) {
                            System.out.println(line);
                        }

                        while ((line = stdError.readLine()) != null) {
                            System.out.println(line);
                        }

                        int exitCode = process.waitFor();
                        if (exitCode != 0) {
                            System.out.println("Error running the Python script. Exit code: " + exitCode);
                        }
                    }
                } else {
                    System.out.println("The table is not empty.");
                }
            } else {
                System.out.println("No result returned from the COUNT query.");
            }

            // Close resources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
