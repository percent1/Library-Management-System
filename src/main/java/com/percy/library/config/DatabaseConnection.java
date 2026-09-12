package com.percy.library.config;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Manages connections between the Library Management System
 * and the MySQL database.
 *
 * The database credentials are stored in the external
 * db.properties file instead of being written directly
 * into the Java source code.
 */
public class DatabaseConnection {

    /*
     * Stores the database configuration loaded from
     * the db.properties file.
     */
    private static final Properties properties = new Properties();

    /*
     * Loads the database configuration when this class
     * is first loaded by the application.
     */
    static {
        try (InputStream input = DatabaseConnection.class
                .getClassLoader()
                .getResourceAsStream("db.properties")) {

            // Check whether the configuration file exists.
            if (input == null) {
                throw new IOException("db.properties file not found.");
            }

            // Load the database settings into the Properties object.
            properties.load(input);

        } catch (IOException e) {
            /*
             * Stop the application if the database configuration
             * cannot be loaded.
             */
            throw new RuntimeException(
                    "Could not load database configuration.", e);
        }
    }

    /**
     * Creates and returns a connection to the LibraryDB database.
     *
     * @return an active database connection
     * @throws SQLException if the database connection cannot be established
     */
    public static Connection getConnection() throws SQLException {

        // Retrieve the database settings from db.properties.
        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");

        // Establish and return the MySQL database connection.
        return DriverManager.getConnection(url, username, password);
    }
}