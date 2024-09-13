package com.sync.api.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class CreateDatabase {
    public static void main(String[] args) {
        String jdbcUrl = Definitions.JDBC_URL;
        String jdbcUser = Definitions.JDBC_USER;
        String jdbcPassword = Definitions.JDBC_PASSWORD;
        String databaseName = "api202402";

        try {
            // Connect to the MySQL server (without specifying the database name)
            Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
            Statement statement = connection.createStatement();

            // Create the database if it does not exist
            String createDatabaseQuery = "CREATE DATABASE IF NOT EXISTS " + databaseName;
            statement.executeUpdate(createDatabaseQuery);
            System.out.println("Database verified/created successfully: " + databaseName);

            // Close resources
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}