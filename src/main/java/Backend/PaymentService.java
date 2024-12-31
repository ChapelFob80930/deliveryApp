package Backend;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PaymentService {
    private Connection connection;

    public PaymentService(Connection connection) {
        this.connection = connection;
    }

    // Add a payment to the database
    public void addPayment(Payment payment) throws SQLException {
        String query = "INSERT INTO payments (OrderId, Amount, SystemCharge, Status, PaymentDate) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, payment.getOrderId());
            ps.setDouble(2, payment.getAmount());
            ps.setDouble(3, payment.getSystemCharge());
            ps.setString(4, payment.getStatus());
            ps.setTimestamp(5, Timestamp.valueOf(payment.getPaymentDate()));
            ps.executeUpdate();
        }
    }

    // Process a payment and store it in the database
    public Payment processPayment(int orderId, double amount) throws SQLException {
        double systemCharge = amount * 0.05; // 5% system charge
        Payment payment = new Payment(0, orderId, amount, systemCharge, "Completed", LocalDateTime.now());
        addPayment(payment);
        return payment;
    }

    // Fetch payment details for a specific order
    public Payment getPaymentDetails(int orderId) throws SQLException {
        String query = "SELECT * FROM payments WHERE OrderId = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Payment(
                            rs.getInt("PaymentId"),
                            rs.getInt("OrderId"),
                            rs.getDouble("Amount"),
                            rs.getDouble("SystemCharge"),
                            rs.getString("Status"),
                            rs.getTimestamp("PaymentDate").toLocalDateTime()
                    );
                }
            }
        }
        return null;
    }

    // Get all payments for a summary view
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
}
