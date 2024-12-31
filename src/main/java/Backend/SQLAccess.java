package Backend;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLAccess {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/deliveryapp";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private Connection connection;

    public SQLAccess() throws SQLException {
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public Connection getConnection() {
        return connection; // Expose connection for services like PaymentService
    }

    public void close() {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add an order
    public void addOrder(Order order) throws SQLException {
        String query = "INSERT INTO orders (RecieverId, SenderId, RecieversAddress, OrderPrice, EstimatedTime, OrderStatus, DetailedDescription) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, order.getReceiverId());
            ps.setInt(2, order.getSenderId());
            ps.setString(3, order.getReceiverAddress());
            ps.setDouble(4, order.getOrderPrice());
            ps.setString(5, order.getEstimatedTime());
            ps.setString(6, order.getOrderStatus());
            ps.setString(7, order.getDetailedDescription());
            ps.executeUpdate();
        }
    }

    // Get all orders
    public List<Order> getAllOrders() throws SQLException {
        String query = "SELECT * FROM orders";
        List<Order> orders = new ArrayList<>();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                orders.add(new Order(
                        rs.getInt("OrderId"),
                        rs.getInt("RecieverId"),
                        rs.getInt("SenderId"),
                        rs.getString("RecieversAddress"),
                        rs.getDouble("OrderPrice"),
                        rs.getString("EstimatedTime"),
                        rs.getString("OrderStatus"),
                        rs.getString("DetailedDescription")
                ));
            }
        }
        return orders;
    }

    // Update order status
    public void updateOrderStatus(int orderId, String status) throws SQLException {
        String query = "UPDATE orders SET OrderStatus = ? WHERE OrderId = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        }
    }

    // Add payment
    public void addPayment(int orderId, double amount, double systemCharge, String status) throws SQLException {
        String query = "INSERT INTO payments (OrderId, Amount, SystemCharge, Status, PaymentDate) VALUES (?, ?, ?, ?, NOW())";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, orderId);
            ps.setDouble(2, amount);
            ps.setDouble(3, systemCharge);
            ps.setString(4, status);
            ps.executeUpdate();
        }
    }

    // Get payment details by orderId
    public ResultSet getPaymentDetails(int orderId) throws SQLException {
        String query = "SELECT * FROM payments WHERE OrderId = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, orderId);
        return ps.executeQuery();
    }

    // Fetch all payments
    public List<Payment> getAllPayments() throws SQLException {
        String query = "SELECT * FROM payments";
        List<Payment> payments = new ArrayList<>();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                payments.add(new Payment(
                        rs.getInt("PaymentId"),
                        rs.getInt("OrderId"),
                        rs.getDouble("Amount"),
                        rs.getDouble("SystemCharge"),
                        rs.getString("Status"),
                        rs.getTimestamp("PaymentDate").toLocalDateTime()
                ));
            }
        }
        return payments;
    }

    public Order getOrderById(int orderId) throws SQLException {
        String query = "SELECT * FROM orders WHERE OrderId = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Order(
                            rs.getInt("OrderId"),
                            rs.getInt("RecieverId"),
                            rs.getInt("SenderId"),
                            rs.getString("RecieversAddress"),
                            rs.getDouble("OrderPrice"),
                            rs.getString("EstimatedTime"),
                            rs.getString("OrderStatus"),
                            rs.getString("DetailedDescription")
                    );
                }
            }
        }
        return null;
    }

}
