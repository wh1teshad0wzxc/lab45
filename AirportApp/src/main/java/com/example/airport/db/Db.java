package com.example.airport.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class Db {

    private static final String URL =
            "jdbc:postgresql://localhost:5432/airport_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    public static Connection connect() throws Exception {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
