package Backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLAccess {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/employee_details";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private Connection connection;

    public SQLAccess() throws SQLException {
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void close() {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
