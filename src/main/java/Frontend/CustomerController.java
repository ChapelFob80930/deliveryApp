package Frontend;

import Backend.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.sql.SQLException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CustomerController {

    @FXML
    private TextField contactField, addressField, priceField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private TableView<Order> ordersTable;

    @FXML
    private TableColumn<Order, Integer> orderIdColumn;

    @FXML
    private TableColumn<Order, String> receiverAddressColumn;

    @FXML
    private TableColumn<Order, Double> orderPriceColumn;

    @FXML
    private TableColumn<Order, String> orderStatusColumn;

    @FXML
    private Button placeOrderButton, paymentButton, trackDeliveryButton;

    @FXML
    private ProgressBar deliveryProgressBar;

    private SQLAccess sqlAccess;
    private int selectedOrderId;
    private DeliveryTrackingService trackingService;
    private NotificationService notificationService;

    public CustomerController() {
        try {
            sqlAccess = new SQLAccess();
            trackingService = new DeliveryTrackingService(sqlAccess.getConnection());
            notificationService = new NotificationService(sqlAccess.getConnection());
        } catch (SQLException e) {
            showError("Database Connection Error", e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        configureTable();
        loadOrders();
        startRealTimeTracking();
    }

    private void configureTable() {
        orderIdColumn.setCellValueFactory(cellData -> cellData.getValue().orderIdProperty().asObject());
        receiverAddressColumn.setCellValueFactory(cellData -> cellData.getValue().receiverAddressProperty());
        orderPriceColumn.setCellValueFactory(cellData -> cellData.getValue().orderPriceProperty().asObject());
        orderStatusColumn.setCellValueFactory(cellData -> cellData.getValue().orderStatusProperty());

        ordersTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedOrderId = newValue.getOrderId();
            }
        });
    }

    @FXML
    private void handlePlaceOrder() {
        try {
            Order order = new Order(
                    0,
                    1,
                    2,
                    addressField.getText(),
                    Double.parseDouble(priceField.getText()),
                    "2 hours",
                    "Pending",
                    descriptionArea.getText()
            );
            sqlAccess.addOrder(order);
            loadOrders();
            clearFields();
            showNotification("Order Placed", "Your order has been placed successfully!");
        } catch (SQLException e) {
            showError("Error Saving Order", e.getMessage());
        } catch (NumberFormatException e) {
            showError("Invalid Input", "Please enter a valid price.");
        }
    }

    @FXML
    private void handleTrackDelivery() {
        try {
            if (selectedOrderId == 0) {
                showError("No Order Selected", "Please select an order to track.");
                return;
            }

            String status = trackingService.getTrackingStatus(selectedOrderId);
            showNotification("Delivery Status", "Your order status is: " + status);

            updateProgressBar(status);
        } catch (SQLException e) {
            showError("Tracking Error", e.getMessage());
        }
    }

    private void loadOrders() {
        try {
            List<Order> orders = sqlAccess.getAllOrders();
            ordersTable.getItems().setAll(orders);
        } catch (SQLException e) {
            showError("Error Loading Orders", e.getMessage());
        }
    }

    private void clearFields() {
        contactField.clear();
        addressField.clear();
        priceField.clear();
        descriptionArea.clear();
    }

    private void showNotification(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updateProgressBar(String status) {
        double progress = switch (status) {
            case "Pending" -> 0.0;
            case "Accepted" -> 0.25;
            case "In Transit" -> 0.75;
            case "Delivered" -> 1.0;
            default -> 0.0;
        };
        deliveryProgressBar.setProgress(progress);
    }

    private void startRealTimeTracking() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        if (selectedOrderId != 0) {
                            String status = trackingService.getTrackingStatus(selectedOrderId);
                            updateProgressBar(status);
                            notificationService.sendNotification(selectedOrderId, "Real-time update: " + status);
                        }
                    } catch (SQLException e) {
                        showError("Real-Time Tracking Error", e.getMessage());
                    }
                });
            }
        }, 0, 5000); // Update every 5 seconds
    }

    @FXML
    private void handlePayment() {
        try {
            if (selectedOrderId == 0) {
                showError("No Order Selected", "Please select an order to make a payment.");
                return;
            }

            PaymentService paymentService = new PaymentService(sqlAccess.getConnection());
            Payment payment = paymentService.processPayment(selectedOrderId, calculateOrderAmount(selectedOrderId));

            showNotification("Payment Successful", "Payment ID: " + payment.getPaymentId() +
                    "\nAmount: " + payment.getAmount() +
                    "\nStatus: " + payment.getStatus());
        } catch (SQLException e) {
            showError("Payment Error", e.getMessage());
        }
    }

    private double calculateOrderAmount(int orderId) throws SQLException {
        Order order = sqlAccess.getOrderById(orderId);
        return order.getOrderPrice() + order.getOrderPrice() * 0.05; // Adding 5% system charge
    }

    @FXML
    private void handleProfileUpdate() {
        try {
            String updatedName = contactField.getText();
            String updatedContact = addressField.getText();

            CustomerProfile profile = new CustomerProfile(sqlAccess.getConnection());
            profile.updateCustomerDetails(1, updatedName, updatedContact);
            showNotification("Profile Updated", "Your profile has been successfully updated.");
        } catch (SQLException e) {
            showError("Profile Update Error", e.getMessage());
        }
    }

    @FXML
    private void handleViewOrderHistory() {
        try {
            CustomerOrderHistory history = new CustomerOrderHistory(sqlAccess.getConnection());
            List<Order> orders = history.getOrderHistory(1);
            ordersTable.getItems().setAll(orders);
        } catch (SQLException e) {
            showError("Order History Error", e.getMessage());
        }
    }
}
