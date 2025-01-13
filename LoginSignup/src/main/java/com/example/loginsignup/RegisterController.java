package com.example.loginsignup;

import com.example.loginsignup.DBrelated.DBConnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.application.Platform;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

public class RegisterController {

    @FXML
    private TextField emailField, nameField, surnameField, phoneField, verificationCodeField;

    @FXML
    private PasswordField passwordField, confirmPasswordField;

    @FXML
    private Button backButton,  signupButton;








    @FXML
    private void handleSignup() {
        try {
            // Retrieve field values
            String email = emailField.getText().trim();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();
            String name = nameField.getText().trim();
            String surname = surnameField.getText().trim();
            String phone = phoneField.getText().trim();

            // Validate fields are not empty
            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
                    name.isEmpty() || surname.isEmpty()) {
                showAlert("Validation Error", "All fields marked with * are required.");
                return;
            }

            // Validate passwords match
            if (!password.equals(confirmPassword)) {
                showAlert("Validation Error", "Passwords do not match.");
                return;
            }

            // Validate email format (optional but recommended)
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                showAlert("Validation Error", "Invalid email format.");
                return;
            }

            // Check if email already exists in the database
            try (Connection connection = DBConnection.getConnection()) {
                String checkEmailQuery = "SELECT Email FROM users WHERE Email = ?";
                PreparedStatement emailCheckStmt = connection.prepareStatement(checkEmailQuery);
                emailCheckStmt.setString(1, email);

                ResultSet resultSet = emailCheckStmt.executeQuery();
                if (resultSet.next()) {
                    showAlert("Validation Error", "This email is already registered.");
                    return;
                }

                // Insert the user into the database
                String insertQuery = "INSERT INTO users (Email, Password, Name, Surname, PhoneNumber) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
                insertStmt.setString(1, email);
                insertStmt.setString(2, password); // Ideally, hash the password before storing
                insertStmt.setString(3, name);
                insertStmt.setString(4, surname);
                insertStmt.setString(5, phone.isEmpty() ? null : phone); // Allow phone to be optional

                int rowsInserted = insertStmt.executeUpdate();
                if (rowsInserted > 0) {
                    showAlert("Success", "Registration successful!");
                    // Optionally redirect to the login page
                    MainApp.openNewStage("login-view.fxml", "Login");
                    Stage stage = (Stage) signupButton.getScene().getWindow();
                    stage.close();
                } else {
                    showAlert("Error", "Failed to register. Please try again.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An unexpected error occurred. Please try again.");
        }
    }




    /**
     Handles the "Back" button click event.
     */
    @FXML
    private void handleBack() {
        try {
            // Load login-view.fxml and close the current stage
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.close();
            MainApp.openNewStage("login-view.fxml", "Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }






    /**
     * Utility method to show an alert.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
