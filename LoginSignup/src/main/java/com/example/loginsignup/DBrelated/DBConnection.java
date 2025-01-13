package com.example.loginsignup.DBrelated;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Database URL, username, and password
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/delapdb"; // Update with your database name
    private static final String USER = "root"; // Replace with your MySQL username
    private static final String PASSWORD = "12345"; // Replace with your MySQL password

    // Private constructor to prevent instantiation
    private DBConnection() {}

    // Method to establish and return a connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

