package Backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeliveryTrackingService {
    private Connection connection;

    public DeliveryTrackingService(Connection connection) {
        this.connection = connection;
    }

    // Update the delivery tracking status
    public void updateTrackingStatus(int orderId, String status) throws SQLException {
        String query = "UPDATE orders SET TrackingStatus = ? WHERE OrderId = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        }
    }

    // Get the current tracking status of an order
    public String getTrackingStatus(int orderId) throws SQLException {
        String query = "SELECT TrackingStatus FROM orders WHERE OrderId = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("TrackingStatus");
                }
            }
        }
        return "Unknown";
    }
}
