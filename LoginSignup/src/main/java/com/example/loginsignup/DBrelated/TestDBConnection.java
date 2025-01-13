package com.example.loginsignup.DBrelated;

import java.sql.Connection;

public class TestDBConnection {
    public static void main(String[] args) {
        try (Connection connection = DBConnection.getConnection()) {
            if (connection != null) {
                System.out.println("Connected to the database successfully!");
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
