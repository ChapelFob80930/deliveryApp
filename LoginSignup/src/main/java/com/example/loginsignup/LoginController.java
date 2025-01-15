package com.example.loginsignup;

import com.example.loginsignup.DBrelated.DBConnection;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    // Link FXML elements
    @FXML
    private TextField emailField; // fx:id="emailField"

    @FXML
    private PasswordField passwordField; // fx:id="passwordField"

    @FXML
    private Button loginButton; // fx:id="loginButton"

    @FXML
    private Button signupButton;

    @FXML
    private RadioButton receiverRadioButton; // fx:id="receiverRadioButton"

    @FXML
    private RadioButton courierRadioButton; // fx:id="courierRadioButton"

    @FXML
    private ToggleGroup modeToggleGroup; // To group the radio buttons

    @FXML
    private void handleLogin() {
        // Step 1: Validate input fields
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Validation Error", "Email or password field cannot be empty.");
            return;
        }

        // Step 2: Connect to the database and verify credentials
        try (Connection connection = DBConnection.getConnection()) { // Keep the connection logic intact
            // Query to check if the email exists
            String emailQuery = "SELECT * FROM users WHERE Email = ?";
            PreparedStatement emailCheckStmt = connection.prepareStatement(emailQuery);
            emailCheckStmt.setString(1, email);
            ResultSet resultSet = emailCheckStmt.executeQuery();

            if (!resultSet.next()) { // If no user found
                showAlert("Login Failed", "No user found with this email.");
                return;
            }

            // Verify the password
            String storedPassword = resultSet.getString("Password");
            if (!storedPassword.equals(password)) {
                showAlert("Login Failed", "Incorrect password.");
                return;
            }

            // Step 3: Determine role from radio buttons
            if (receiverRadioButton.isSelected()) {

                userSession.setUserEmail(email);
                // Redirect to the receiver page
                MainApp.openNewStage("reciever.fxml", "Receiver Dashboard");

                // Close the current login window
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.close();
            } else if (courierRadioButton.isSelected()) {
                // Handle the courier case (future implementation)
                showAlert("Login Successful", "Courier functionality not yet implemented.");
            } else {
                showAlert("Role Selection Error", "Please select a role (Receiver or Courier).");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "An error occurred while connecting to the database.");
        }
    }
    @FXML
    private void handleSignUpRedirect() {
        try {
            // Switch to the registration page
            MainApp.openNewStage("signup-view.fxml", "Sign Up");

            // Close the current login window
            Stage stage = (Stage) signupButton.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Utility method to show alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
