package com.example.bd.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Sesuaikan URL, user, dan password dengan konfigurasi PostgreSQL Anda
    private static final String URL = "jdbc:postgresql://localhost:5432/projek";
    private static final String USER = "postgres";
    private static final String PASSWORD = "admin";

    private static Connection connection;

    // Private constructor agar tidak bisa diinstansiasi dari luar
    private DatabaseConnection() {}

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (SQLException e) {
                e.printStackTrace();
                // Di aplikasi nyata, tampilkan pesan error yang lebih user-friendly
            }
        }


        return connection;
    }
}