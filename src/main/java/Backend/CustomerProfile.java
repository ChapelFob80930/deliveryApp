package Backend;

import java.sql.*;

public class CustomerProfile {
    private Connection connection;

    public CustomerProfile(Connection connection) {
        this.connection = connection;
    }

    public void updateCustomerDetails(int customerId, String name, String contact) throws SQLException {
        String query = "UPDATE customers SET Name = ?, Contact = ? WHERE CustomerId = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setString(2, contact);
            ps.setInt(3, customerId);
            ps.executeUpdate();
        }
    }

    public Customer getCustomerDetails(int customerId) throws SQLException {
        String query = "SELECT * FROM customers WHERE CustomerId = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                            rs.getInt("CustomerId"),
                            rs.getString("Name"),
                            rs.getString("Contact")
                    );
                }
            }
        }
        return null;
    }
}
