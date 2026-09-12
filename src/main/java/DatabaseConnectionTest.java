import java.sql.Connection;
import java.sql.SQLException;
import com.percy.library.config.DatabaseConnection;

/**
 * Tests the connection between the Library Management System
 * and the MySQL database.
 */
public class DatabaseConnectionTest {

    /**
     * Attempts to establish a database connection
     * and displays the result.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {

        try (Connection connection = DatabaseConnection.getConnection()) {

            // Confirm that the database connection was successful.
            System.out.println("Database connection successful!");

        } catch (SQLException e) {

            // Display an error message if the connection fails.
            System.out.println("Database connection failed.");
            e.printStackTrace();
        }
    }
}