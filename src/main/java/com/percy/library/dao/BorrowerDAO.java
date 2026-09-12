package com.percy.library.dao;

import com.percy.library.config.DatabaseConnection;
import com.percy.library.model.Borrower;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides database operations for Borrower objects.
 *
 * The DAO (Data Access Object) separates database operations
 * from the rest of the application.
 */
public class BorrowerDAO {

    /**
     * Retrieves all borrowers from the database.
     *
     * @return a list containing all borrowers
     */
    public List<Borrower> getAllBorrowers() {

        String sql = "SELECT * FROM borrowers";

        List<Borrower> borrowers = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Borrower borrower = new Borrower(
                        resultSet.getInt("borrower_id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone")
                );

                borrowers.add(borrower);
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Could not retrieve borrowers.",
                    e
            );
        }

        return borrowers;
    }

    /**
     * Retrieves a borrower by their ID.
     *
     * @param borrowerId unique ID of the borrower
     * @return the matching Borrower, or null if none exists
     */
    public Borrower getBorrowerById(int borrowerId) {

        String sql = "SELECT * FROM borrowers WHERE borrower_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, borrowerId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    return new Borrower(
                            resultSet.getInt("borrower_id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getString("email"),
                            resultSet.getString("phone")
                    );
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Could not retrieve borrower with ID "
                            + borrowerId + ".",
                    e
            );
        }

        return null;
    }

    /**
     * Adds a new borrower to the database.
     *
     * @param borrower borrower to add
     * @return generated borrower ID
     */
    public int addBorrower(Borrower borrower) {

        String sql = """
                INSERT INTO borrowers
                (first_name, last_name, email, phone)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql,
                     java.sql.Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, borrower.getFirstName());
            statement.setString(2, borrower.getLastName());
            statement.setString(3, borrower.getEmail());
            statement.setString(4, borrower.getPhone());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new RuntimeException("Borrower was not added.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Could not add borrower.",
                    e
            );
        }

        throw new RuntimeException(
                "Borrower was added, but no ID was returned."
        );
    }

    /**
     * Updates an existing borrower.
     *
     * @param borrower borrower containing the updated information
     * @return true if the borrower was updated
     */
    public boolean updateBorrower(Borrower borrower) {

        String sql = """
                UPDATE borrowers
                SET first_name = ?,
                    last_name = ?,
                    email = ?,
                    phone = ?
                WHERE borrower_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, borrower.getFirstName());
            statement.setString(2, borrower.getLastName());
            statement.setString(3, borrower.getEmail());
            statement.setString(4, borrower.getPhone());
            statement.setInt(5, borrower.getBorrowerId());

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Could not update borrower with ID "
                            + borrower.getBorrowerId() + ".",
                    e
            );
        }
    }

    /**
     * Deletes a borrower from the database.
     *
     * @param borrowerId unique ID of the borrower to delete
     * @return true if the borrower was deleted
     */
    public boolean deleteBorrower(int borrowerId) {

        String sql = "DELETE FROM borrowers WHERE borrower_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, borrowerId);

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Could not delete borrower with ID "
                            + borrowerId + ".",
                    e
            );
        }
    }
}