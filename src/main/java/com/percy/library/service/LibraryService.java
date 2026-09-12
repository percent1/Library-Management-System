package com.percy.library.service;

import com.percy.library.config.DatabaseConnection;
import com.percy.library.dao.BookDAO;
import com.percy.library.dao.BorrowerDAO;
import com.percy.library.dao.LoanDAO;
import com.percy.library.model.Book;
import com.percy.library.model.Borrower;
import com.percy.library.model.Loan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Handles the business rules for borrowing and returning books.
 *
 * The service layer sits between the application and the DAOs.
 * It makes sure that related database operations are performed
 * together so that the library data remains consistent.
 */
public class LibraryService {

    private final BookDAO bookDAO;
    private final BorrowerDAO borrowerDAO;
    private final LoanDAO loanDAO;

    /**
     * Creates a LibraryService using the application's DAOs.
     */
    public LibraryService() {
        this.bookDAO = new BookDAO();
        this.borrowerDAO = new BorrowerDAO();
        this.loanDAO = new LoanDAO();
    }

    /**
     * Borrows a book for a borrower.
     *
     * The operation:
     * 1. Checks that the book exists.
     * 2. Checks that the book is available.
     * 3. Checks that the borrower exists.
     * 4. Creates a loan.
     * 5. Marks the book as unavailable.
     *
     * All database changes are performed in one transaction.
     *
     * @param bookId the ID of the book being borrowed
     * @param borrowerId the ID of the borrower
     * @return the newly created loan ID
     */
    public int borrowBook(int bookId, int borrowerId) {

        Book book = bookDAO.getBookById(bookId);

        if (book == null) {
            throw new IllegalArgumentException(
                    "Book with ID " + bookId + " does not exist."
            );
        }

        if (!book.isAvailable()) {
            throw new IllegalStateException(
                    "Book with ID " + bookId + " is already borrowed."
            );
        }

        Borrower borrower = borrowerDAO.getBorrowerById(borrowerId);

        if (borrower == null) {
            throw new IllegalArgumentException(
                    "Borrower with ID " + borrowerId + " does not exist."
            );
        }

        String insertLoanSql = """
                INSERT INTO loans
                (book_id, borrower_id, loan_date, due_date)
                VALUES (?, ?, ?, ?)
                """;

        String updateBookSql = """
                UPDATE books
                SET available = FALSE
                WHERE book_id = ?
                """;

        LocalDate loanDate = LocalDate.now();
        LocalDate dueDate = loanDate.plusDays(14);

        try (Connection connection = DatabaseConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {
                int loanId;

                /*
                 * Create the loan record.
                 */
                try (PreparedStatement statement =
                             connection.prepareStatement(
                                     insertLoanSql,
                                     java.sql.Statement.RETURN_GENERATED_KEYS)) {

                    statement.setInt(1, bookId);
                    statement.setInt(2, borrowerId);
                    statement.setDate(
                            3,
                            java.sql.Date.valueOf(loanDate)
                    );
                    statement.setDate(
                            4,
                            java.sql.Date.valueOf(dueDate)
                    );

                    statement.executeUpdate();

                    try (ResultSet resultSet =
                                 statement.getGeneratedKeys()) {

                        if (!resultSet.next()) {
                            throw new SQLException(
                                    "Could not retrieve the new loan ID."
                            );
                        }

                        loanId = resultSet.getInt(1);
                    }
                }

                /*
                 * Mark the book as unavailable.
                 */
                try (PreparedStatement statement =
                             connection.prepareStatement(updateBookSql)) {

                    statement.setInt(1, bookId);

                    int rowsUpdated = statement.executeUpdate();

                    if (rowsUpdated == 0) {
                        throw new SQLException(
                                "Could not update book availability."
                        );
                    }
                }

                connection.commit();

                return loanId;

            } catch (SQLException e) {

                connection.rollback();

                throw new RuntimeException(
                        "Could not borrow the book.",
                        e
                );

            } finally {

                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Could not connect to the database.",
                    e
            );
        }
    }

    /**
     * Returns a borrowed book.
     *
     * The operation:
     * 1. Finds the loan.
     * 2. Checks that the loan exists.
     * 3. Checks that the book has not already been returned.
     * 4. Records the return date.
     * 5. Marks the book as available.
     *
     * All database changes are performed in one transaction.
     *
     * @param loanId the ID of the loan being returned
     */
    public void returnBook(int loanId) {

        Loan loan = loanDAO.getLoanById(loanId);

        if (loan == null) {
            throw new IllegalArgumentException(
                    "Loan with ID " + loanId + " does not exist."
            );
        }

        if (loan.getReturnDate() != null) {
            throw new IllegalStateException(
                    "Loan with ID " + loanId + " has already been returned."
            );
        }

        String updateLoanSql = """
                UPDATE loans
                SET return_date = ?
                WHERE loan_id = ?
                AND return_date IS NULL
                """;

        String updateBookSql = """
                UPDATE books
                SET available = TRUE
                WHERE book_id = ?
                """;

        LocalDate returnDate = LocalDate.now();

        try (Connection connection = DatabaseConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {

                /*
                 * Record the return date.
                 */
                try (PreparedStatement statement =
                             connection.prepareStatement(updateLoanSql)) {

                    statement.setDate(
                            1,
                            java.sql.Date.valueOf(returnDate)
                    );
                    statement.setInt(2, loanId);

                    int rowsUpdated = statement.executeUpdate();

                    if (rowsUpdated == 0) {
                        throw new SQLException(
                                "Could not update the loan."
                        );
                    }
                }

                /*
                 * Make the book available again.
                 */
                try (PreparedStatement statement =
                             connection.prepareStatement(updateBookSql)) {

                    statement.setInt(1, loan.getBookId());

                    int rowsUpdated = statement.executeUpdate();

                    if (rowsUpdated == 0) {
                        throw new SQLException(
                                "Could not update book availability."
                        );
                    }
                }

                connection.commit();

            } catch (SQLException e) {

                connection.rollback();

                throw new RuntimeException(
                        "Could not return the book.",
                        e
                );

            } finally {

                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Could not connect to the database.",
                    e
            );
        }
    }
}