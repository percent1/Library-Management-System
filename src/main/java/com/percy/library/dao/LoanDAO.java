
package com.percy.library.dao;

import com.percy.library.config.DatabaseConnection;
import com.percy.library.model.Loan;
import com.percy.library.model.LoanDetails;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides database operations for Loan objects.
 *
 * The DAO (Data Access Object) separates database operations
 * from the rest of the application.
 */
public class LoanDAO {

    /**
     * Retrieves all loans from the database.
     *
     * @return a list containing all loans
     */
    public List<Loan> getAllLoans() {

        String sql = "SELECT * FROM loans";

        List<Loan> loans = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Loan loan = new Loan(
                        resultSet.getInt("loan_id"),
                        resultSet.getInt("book_id"),
                        resultSet.getInt("borrower_id"),
                        resultSet.getDate("loan_date").toLocalDate(),
                        resultSet.getDate("due_date").toLocalDate(),
                        resultSet.getDate("return_date") != null
                                ? resultSet.getDate("return_date").toLocalDate()
                                : null
                );

                loans.add(loan);
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Could not retrieve loans.",
                    e
            );
        }

        return loans;
    }

    /**
     * Retrieves a loan using its ID.
     *
     * @param loanId the ID of the loan
     * @return the matching loan, or null if it does not exist
     */
    public Loan getLoanById(int loanId) {

        String sql = "SELECT * FROM loans WHERE loan_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, loanId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    return new Loan(
                            resultSet.getInt("loan_id"),
                            resultSet.getInt("book_id"),
                            resultSet.getInt("borrower_id"),
                            resultSet.getDate("loan_date").toLocalDate(),
                            resultSet.getDate("due_date").toLocalDate(),
                            resultSet.getDate("return_date") != null
                                    ? resultSet.getDate("return_date").toLocalDate()
                                    : null
                    );
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Could not retrieve loan with ID "
                            + loanId + ".",
                    e
            );
        }

        return null;
    }

    /**
     * Adds a new loan to the database.
     *
     * @param loan the loan to add
     * @return the generated loan ID
     */
    public int addLoan(Loan loan) {

        String sql = """
                INSERT INTO loans
                (book_id, borrower_id, loan_date, due_date, return_date)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql,
                     java.sql.Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, loan.getBookId());
            statement.setInt(2, loan.getBorrowerId());
            statement.setDate(3, Date.valueOf(loan.getLoanDate()));
            statement.setDate(4, Date.valueOf(loan.getDueDate()));

            if (loan.getReturnDate() != null) {

                statement.setDate(
                        5,
                        Date.valueOf(loan.getReturnDate())
                );

            } else {

                statement.setNull(
                        5,
                        java.sql.Types.DATE
                );
            }

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new RuntimeException("Loan was not added.");
            }

            try (ResultSet generatedKeys =
                         statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Could not add loan.",
                    e
            );
        }

        throw new RuntimeException(
                "Loan was added, but no ID was returned."
        );
    }

    /**
     * Updates an existing loan.
     *
     * @param loan the loan containing updated information
     * @return true if the loan was updated successfully
     */
    public boolean updateLoan(Loan loan) {

        String sql = """
                UPDATE loans
                SET book_id = ?,
                    borrower_id = ?,
                    loan_date = ?,
                    due_date = ?,
                    return_date = ?
                WHERE loan_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, loan.getBookId());
            statement.setInt(2, loan.getBorrowerId());
            statement.setDate(3, Date.valueOf(loan.getLoanDate()));
            statement.setDate(4, Date.valueOf(loan.getDueDate()));

            if (loan.getReturnDate() != null) {

                statement.setDate(
                        5,
                        Date.valueOf(loan.getReturnDate())
                );

            } else {

                statement.setNull(
                        5,
                        java.sql.Types.DATE
                );
            }

            statement.setInt(6, loan.getLoanId());

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Could not update loan with ID "
                            + loan.getLoanId() + ".",
                    e
            );
        }
    }

    /**
     * Deletes a loan using its ID.
     *
     * @param loanId the ID of the loan to delete
     * @return true if the loan was deleted successfully
     */
    public boolean deleteLoan(int loanId) {

        String sql = "DELETE FROM loans WHERE loan_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, loanId);

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Could not delete loan with ID "
                            + loanId + ".",
                    e
            );
        }
    }

    /**
     * Retrieves all loans together with the related
     * book title and borrower name.
     *
     * This method joins the loans, books, and borrowers
     * tables so the user interface can display meaningful
     * information instead of only database IDs.
     *
     * @return a list containing detailed loan information
     */
    public List<LoanDetails> getAllLoanDetails() {

        String sql = """
                SELECT
                    l.loan_id,
                    b.title AS book_title,
                    CONCAT(br.first_name, ' ', br.last_name) AS borrower_name,
                    l.loan_date,
                    l.due_date,
                    l.return_date
                FROM loans l
                JOIN books b
                    ON l.book_id = b.book_id
                JOIN borrowers br
                    ON l.borrower_id = br.borrower_id
                ORDER BY l.loan_id
                """;

        List<LoanDetails> loanDetails = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                LoanDetails details = new LoanDetails(
                        resultSet.getInt("loan_id"),
                        resultSet.getString("book_title"),
                        resultSet.getString("borrower_name"),
                        resultSet.getDate("loan_date").toLocalDate(),
                        resultSet.getDate("due_date").toLocalDate(),
                        resultSet.getDate("return_date") != null
                                ? resultSet.getDate("return_date").toLocalDate()
                                : null
                );

                loanDetails.add(details);
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Could not retrieve detailed loan information.",
                    e
            );
        }

        return loanDetails;
    }
}
