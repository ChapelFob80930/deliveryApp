package com.example.loginsignup;

import com.example.loginsignup.DBrelated.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class OrderPlacementController {

    private int userId; // Variable to store the UserId

    public void setUserId(int userId) {
        this.userId = userId;

    }

    @FXML
    private Button backButton; // Back button

    @FXML
    private Button submitOrderButton; // Submit button

    @FXML
    private TextField deliveryAddressField;

    @FXML
    private TextField orderPriceField;

    @FXML
    private TextField deliveryTimeField;

    @FXML
    private TextArea orderDescriptionArea;

    @FXML
    private void handleBack() {
        try {
            // Load the previous scene (e.g., Receiver Dashboard)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("reciever.fxml")); // Adjust file name if needed
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Receiver Dashboard");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSubmitOrder() {
        System.out.println("User ID: " + userId);

        // Retrieve input values
        String deliveryAddress = deliveryAddressField.getText().trim();
        String orderPriceText = orderPriceField.getText().trim();
        String deliveryTimeText = deliveryTimeField.getText().trim();
        String orderDescription = orderDescriptionArea.getText().trim();

        // Check for empty fields
        if (deliveryAddress.isEmpty() || orderPriceText.isEmpty() || deliveryTimeText.isEmpty() || orderDescription.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText("All fields are required.");
            alert.setContentText("Please fill in all fields before submitting the order.");
            alert.showAndWait();
            return;
        }

        double orderPrice;
        int deliveryTime;

        try {
            orderPrice = Double.parseDouble(orderPriceText);
            deliveryTime = Integer.parseInt(deliveryTimeText);

            // Perform further validations
            if (orderPrice <= 0 || deliveryTime <= 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Validation Error");
                alert.setHeaderText("Invalid numeric values.");
                alert.setContentText("Price and delivery time must be positive numbers.");
                alert.showAndWait();
                return;
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Invalid input format.");
            alert.setContentText("Please enter valid numeric values for price and delivery time.");
            alert.showAndWait();
            return;
        }

        // Insert order into the database
        try (Connection connection = DBConnection.getConnection()) {
            String insertQuery = "INSERT INTO orders (ReceiverId, SenderId, ReceiversAddress, OrderPrice, EstimatedTime, OrderStatus, DetailedDescription, OrderDate) " +
                    "VALUES (?, NULL, ?, ?, ?, 'Pending', ?, CURDATE())";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, userId); // ReceiverId
            preparedStatement.setString(2, deliveryAddress); // ReceiversAddress
            preparedStatement.setDouble(3, orderPrice); // OrderPrice
            preparedStatement.setInt(4, deliveryTime); // EstimatedTime
            preparedStatement.setString(5, orderDescription); // DetailedDescription

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Order Submitted");
                successAlert.setHeaderText("Your order has been submitted successfully!");
                successAlert.setContentText("Order details have been saved to the database.");
                successAlert.showAndWait();

                // Clear fields after submission
//                deliveryAddressField.clear();
//                orderPriceField.clear();
//                deliveryTimeField.clear();
//                orderDescriptionArea.clear();
                // Trigger the back button's action
                backButton.fire();
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Submission Failed");
                errorAlert.setHeaderText("Order submission failed.");
                errorAlert.setContentText("Please try again.");
                errorAlert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert dbErrorAlert = new Alert(Alert.AlertType.ERROR);
            dbErrorAlert.setTitle("Database Error");
            dbErrorAlert.setHeaderText("An error occurred while interacting with the database.");
            dbErrorAlert.setContentText(e.getMessage());
            dbErrorAlert.showAndWait();
        }
    }


}
