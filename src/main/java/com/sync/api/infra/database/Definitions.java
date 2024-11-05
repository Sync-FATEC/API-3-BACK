package com.sync.api.infra.database;

import io.github.cdimascio.dotenv.Dotenv;

public class Definitions {
    private static final Dotenv dotenv = Dotenv.load();

    public static final String JDBC_URL = dotenv.get("JDBC_URL");
    public static final String JDBC_USER = dotenv.get("JDBC_USER");
    public static final String JDBC_PASSWORD = dotenv.get("JDBC_PASSWORD");
}
