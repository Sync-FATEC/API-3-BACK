package com.sync.api.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class AddDataToDatabase {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/api202402";
        String jdbcUser = Definitions.JDBC_USER;
        String jdbcPassword = Definitions.JDBC_PASSWORD;
        String tableName = "projects";
        String pythonInterpreter = "python"; // Or "python" depending on your system
        String scriptPath = "informacoesBanco/adicionar_dados_no_banco.py";
        String requirementsPath = "informacoesBanco/requirements.txt"; // Path to requirements file

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
                    // Install Python dependencies using pip
                    ProcessBuilder pipInstall = new ProcessBuilder(pythonInterpreter, "-m", "pip", "install", "-r", requirementsPath);
                    Process pipProcess = pipInstall.start();

                    // Read pip install output
                    BufferedReader pipStdInput = new BufferedReader(new InputStreamReader(pipProcess.getInputStream()));
                    BufferedReader pipStdError = new BufferedReader(new InputStreamReader(pipProcess.getErrorStream()));

                    String pipLine;
                    System.out.println("Pip install output:");
                    while ((pipLine = pipStdInput.readLine()) != null) {
                        System.out.println(pipLine);
                    }

                    System.out.println("Pip install errors (if any):");
                    while ((pipLine = pipStdError.readLine()) != null) {
                        System.out.println(pipLine);
                    }

                    // Wait for pip process to finish and check exit code
                    int pipExitCode = pipProcess.waitFor();
                    System.out.println("Pip install exit code: " + pipExitCode);

                    if (pipExitCode != 0) {
                        System.out.println("Error during pip install. Exit code: " + pipExitCode);
                    }

                    // Run the Python script
                    ProcessBuilder pb = new ProcessBuilder(pythonInterpreter, scriptPath);
                    Process process = pb.start();

                    // Read the standard output of the process (stdout)
                    BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    // Read the error output of the process (stderr)
                    BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

                    String line;
                    while ((line = stdInput.readLine()) != null) {
                        System.out.println(line);
                    }

                    while ((line = stdError.readLine()) != null) {
                        System.out.println(line);
                    }

                    // Wait for the process to complete and check the exit code
                    int exitCode = process.waitFor();

                    if (exitCode != 0) {
                        System.out.println("Error running the Python script. Exit code: " + exitCode);
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
