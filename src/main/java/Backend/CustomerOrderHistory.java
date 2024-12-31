package Backend;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerOrderHistory {
    private Connection connection;

    public CustomerOrderHistory(Connection connection) {
        this.connection = connection;
    }

    public List<Order> getOrderHistory(int customerId) throws SQLException {
        String query = "SELECT * FROM orders WHERE RecieverId = ?";
        List<Order> orders = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
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
        }
        return orders;
    }
}
