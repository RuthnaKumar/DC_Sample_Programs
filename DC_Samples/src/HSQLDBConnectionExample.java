import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class HSQLDBConnectionExample {
    public static void main(String[] args) {
        Connection connection = null;
        try {
            // Load the HSQLDB JDBC driver
            Class.forName("org.hsqldb.jdbc.JDBCDriver");

            // Establish the connection
            String dbUrl = "jdbc:hsqldb:hsql://localhost/webgoat;ifexists=true";
            String username = "SA";
            String password = "";
            connection = DriverManager.getConnection(dbUrl, username, password);

            // Check if the connection is successful
            if (connection != null) {
                System.out.println("Connected to the database.");
                // Perform database operations here
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("HSQLDB JDBC driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database.");
            e.printStackTrace();
        } finally {
            // Close the connection
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
