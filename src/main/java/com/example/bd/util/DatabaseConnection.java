package com.example.bd.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/projek";
    private static final String USER = "postgres";
    private static final String PASSWORD = "admin";

    private static Connection connection;


    private DatabaseConnection() {}

    public static Connection getConnection() {
        if (connection == null) {
            try {

                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (SQLException e) {

                System.err.println("Koneksi database gagal! Periksa URL, user, password, dan pastikan database sudah berjalan.");
                e.printStackTrace();
            }
        }
        return connection;
    }
}