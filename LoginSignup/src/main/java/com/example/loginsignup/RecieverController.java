package com.example.loginsignup;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.SQLException;
import java.util.Optional;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.example.loginsignup.DBrelated.DBConnection;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import java.io.IOException;

public class RecieverController {


    @FXML
    private TableView<Order> ordersTable;
    @FXML
    private TableColumn<Order, String> courierNameColumn;
    @FXML
    private TableColumn<Order, String> courierPhoneColumn;
    @FXML
    private TableColumn<Order, String> receiverAddressColumn;
    @FXML
    private TableColumn<Order, Double> orderPriceColumn;
    @FXML
    private TableColumn<Order, String> orderStatusColumn;
    @FXML
    private TableColumn<Order, String> descriptionColumn;

    // Static Label: "Your email:"
    @FXML
    private Label staticEmailLabel;

    // Dynamic Label: Will display the user's email
    @FXML
    private Label emailLabel;

    // Buttons
    @FXML
    private Button logoutButton;
    @FXML
    private Button viewOrdersButton;
    @FXML
    private Button deliveredButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button viewHistoryButton;
    @FXML
    private Button placeOrderButton;



    // Initialization logic when the view is loaded
    @FXML
    public void initialize() {
        emailLabel.setText(userSession.getUserEmail());
        courierNameColumn.setCellValueFactory(new PropertyValueFactory<>("courierName"));
        courierPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("courierPhone"));
        receiverAddressColumn.setCellValueFactory(new PropertyValueFactory<>("receiverAddress"));
        orderPriceColumn.setCellValueFactory(new PropertyValueFactory<>("orderPrice"));
        orderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        ordersTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && "Accepted".equals(newValue.getOrderStatus())) {
                deliveredButton.setDisable(false); // Enable button if status is 'Accepted'
            } else {
                deliveredButton.setDisable(true); // Disable button otherwise
            }
        });

        // Add a listener for selection changes in the TableView
        ordersTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            cancelButton.setDisable(newValue == null); // Disable the button if no order is selected
        });
    }

    // Event handler for "Log out" button
    @FXML
    private void handleLogout() {
        // Confirmation dialog for logout
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Logout");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to log out?");

        // Wait for user response
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // User confirmed logout
            try {
                // Redirect to the login page
                MainApp.openNewStage("login-view.fxml", "Login");

                // Close the current receiver page
                Stage stage = (Stage) logoutButton.getScene().getWindow();
                stage.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // User canceled logout
            System.out.println("Logout canceled.");
        }
    }

    // Event handler for "View my current orders" button
    @FXML
    private void handleViewOrders() {
        String currentUserEmail = userSession.getUserEmail();

        try (Connection connection = DBConnection.getConnection()) {
            // Get the current user's ID using their email
            String userQuery = "SELECT UserId FROM users WHERE Email = ?";
            PreparedStatement userStmt = connection.prepareStatement(userQuery);
            userStmt.setString(1, currentUserEmail);
            ResultSet userResult = userStmt.executeQuery();

            if (userResult.next()) {
                int currentUserId = userResult.getInt("UserId");

                // Query to fetch orders for the current user
                String ordersQuery = """
    SELECT 
        o.OrderId, o.ReceiversAddress, o.OrderPrice, o.OrderStatus, o.DetailedDescription,
        u.Name AS CourierName, u.Surname AS CourierSurname, u.PhoneNumber AS CourierPhone
    FROM orders o
    LEFT JOIN users u ON o.SenderId = u.UserId
    WHERE o.ReceiverId = ? AND o.OrderStatus IN ('Pending', 'Accepted')
""";
                PreparedStatement ordersStmt = connection.prepareStatement(ordersQuery);
                ordersStmt.setInt(1, currentUserId);
                ResultSet ordersResult = ordersStmt.executeQuery();

                ObservableList<Order> ordersList = FXCollections.observableArrayList();

                while (ordersResult.next()) {
                    int orderId = ordersResult.getInt("OrderId"); // Fetch OrderId
                    String courierName = ordersResult.getString("CourierName");
                    String courierSurname = ordersResult.getString("CourierSurname");
                    String courierPhone = ordersResult.getString("CourierPhone");
                    String receiversAddress = ordersResult.getString("ReceiversAddress");
                    double orderPrice = ordersResult.getDouble("OrderPrice");
                    String orderStatus = ordersResult.getString("OrderStatus");
                    String description = ordersResult.getString("DetailedDescription");

                    String fullCourierName = (courierName != null && courierSurname != null)
                            ? courierName + " " + courierSurname
                            : null;

                    // Add the Order object to the list with orderId
                    ordersList.add(new Order(orderId, fullCourierName, courierPhone, receiversAddress, orderPrice, orderStatus, description));
                }

                // Set the TableView items
                ordersTable.setItems(ordersList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // Event handler for "Delivered" button
    @FXML
    private void handleDelivered() {
        // Get the selected order from the TableView
        Order selectedOrder = ordersTable.getSelectionModel().getSelectedItem();

        // Ensure a valid order is selected, and its status is 'Accepted'
        if (selectedOrder != null && "Accepted".equals(selectedOrder.getOrderStatus())) {
            try (Connection connection = DBConnection.getConnection()) {
                // Update the order's status in the database
                String updateQuery = "UPDATE orders SET OrderStatus = 'Delivered' WHERE OrderId = ?";
                PreparedStatement stmt = connection.prepareStatement(updateQuery);
                stmt.setInt(1, selectedOrder.getOrderId()); // Use OrderId to identify the record

                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    // Update the local object's status
                    selectedOrder.setOrderStatus("Delivered");

                    // Refresh the TableView to reflect changes
                    ordersTable.refresh();

                    // Disable the "Delivered" button after updating
                    deliveredButton.setDisable(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Event handler for "Cancel" button
    @FXML
    private void handleCancel() {
        // Get the selected order
        Order selectedOrder = ordersTable.getSelectionModel().getSelectedItem();

        if (selectedOrder != null) {
            try (Connection connection = DBConnection.getConnection()) {
                if ("Pending".equals(selectedOrder.getOrderStatus())) {
                    // Delete the record from the database if status is Pending
                    String deleteQuery = "DELETE FROM orders WHERE OrderId = ?";
                    PreparedStatement stmt = connection.prepareStatement(deleteQuery);
                    stmt.setInt(1, selectedOrder.getOrderId());

                    int rowsAffected = stmt.executeUpdate();

                    if (rowsAffected > 0) {
                        // Remove the order from the TableView
                        ordersTable.getItems().remove(selectedOrder);
                    }
                } else if ("Accepted".equals(selectedOrder.getOrderStatus())) {
                    // Update the status to Canceled if status is Accepted
                    String updateQuery = "UPDATE orders SET OrderStatus = 'Canceled' WHERE OrderId = ?";
                    PreparedStatement stmt = connection.prepareStatement(updateQuery);
                    stmt.setInt(1, selectedOrder.getOrderId());

                    int rowsAffected = stmt.executeUpdate();

                    if (rowsAffected > 0) {
                        // Update the local object's status
                        selectedOrder.setOrderStatus("Canceled");

                        // Refresh the TableView to reflect changes
                        ordersTable.refresh();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Event handler for "View orders history" button
    @FXML
    private void handleViewHistory() {
        System.out.println("View orders history clicked!");
        // Add logic to display the order history
    }

    // Event handler for "Place an order" button
    @FXML
    private void handlePlaceOrder() {
        try {
            // Get the user's email from the label or session
            String currentUserEmail = userSession.getUserEmail(); // Or emailLabel.getText();

            // Query the UserId using the email
            int currentUserId = 0;
            try (Connection connection = DBConnection.getConnection()) {
                String query = "SELECT UserId FROM users WHERE Email = ?";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, currentUserEmail);
                ResultSet resultSet = stmt.executeQuery();
                if (resultSet.next()) {
                    currentUserId = resultSet.getInt("UserId");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Load the Order Placement scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("OrderPlacement.fxml"));
            Parent root = loader.load();

            // Pass the UserId to the Order Placement controller
            OrderPlacementController orderPlacementController = loader.getController();
            orderPlacementController.setUserId(currentUserId);

            // Get the current stage and set the new scene
            Stage stage = (Stage) placeOrderButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Place an Order");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
