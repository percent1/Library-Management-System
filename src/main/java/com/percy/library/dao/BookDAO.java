package com.percy.library.dao;

import com.percy.library.config.DatabaseConnection;
import com.percy.library.model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides database operations for Book objects.
 *
 * The DAO (Data Access Object) separates database code
 * from the rest of the application.
 */
public class BookDAO {

    /**
     * Retrieves all books from the database.
     *
     * @return a list containing all books
     */
    public List<Book> getAllBooks() {

        // SQL query used to retrieve all books.
        String sql = "SELECT * FROM books";

        // List that will store the Book objects.
        List<Book> books = new ArrayList<>();

        /*
         * Open a database connection and prepare the SQL query.
         * try-with-resources automatically closes these resources.
         */
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            /*
             * Move through each row returned by MySQL.
             */
            while (resultSet.next()) {

                /*
                 * Convert the current database row into
                 * a Book object.
                 */
                Book book = new Book(
                        resultSet.getInt("book_id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("isbn"),
                        resultSet.getString("genre"),
                        resultSet.getObject("publication_year", Integer.class),
                        resultSet.getBoolean("available")
                );

                // Add the Book object to the list.
                books.add(book);
            }

        } catch (SQLException e) {

            /*
             * Convert the database error into a runtime exception
             * so the calling code knows the database operation failed.
             */
            throw new RuntimeException("Could not retrieve books.", e);
        }

        return books;
    }

    /**
 * Retrieves a book by its ID.
 *
 * @param bookId unique ID of the book
 * @return the matching Book, or null if no book exists
 */
public Book getBookById(int bookId) {

    // The ? is a placeholder for the book ID.
    String sql = "SELECT * FROM books WHERE book_id = ?";

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

        // Replace the ? with the book ID provided to the method.
        statement.setInt(1, bookId);

        // Execute the SELECT query.
        try (ResultSet resultSet = statement.executeQuery()) {

            // Check whether a matching book was found.
            if (resultSet.next()) {

                // Convert the database row into a Book object.
                return new Book(
                        resultSet.getInt("book_id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("isbn"),
                        resultSet.getString("genre"),
                        resultSet.getObject(
                                "publication_year",
                                Integer.class
                        ),
                        resultSet.getBoolean("available")
                );
            }
        }

    } catch (SQLException e) {

        // Report the database error to the calling code.
        throw new RuntimeException(
                "Could not retrieve book with ID " + bookId + ".",
                e
        );
    }

    // Return null when no book with the requested ID exists.
    return null;
}

/**
 * Adds a new book to the database.
 *
 * @param book the Book object to add
 * @return the generated ID assigned to the new book
 */
public int addBook(Book book) {

    // SQL statement used to insert a new book.
    String sql = """
            INSERT INTO books
            (title, author, isbn, genre, publication_year, available)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(
                 sql,
                 java.sql.Statement.RETURN_GENERATED_KEYS)) {

        // Set the values for each ? placeholder.
        statement.setString(1, book.getTitle());
        statement.setString(2, book.getAuthor());
        statement.setString(3, book.getIsbn());
        statement.setString(4, book.getGenre());
        statement.setObject(5, book.getPublicationYear());
        statement.setBoolean(6, book.isAvailable());

        // Execute the INSERT statement.
        int rowsAffected = statement.executeUpdate();

        // Check that a book was actually inserted.
        if (rowsAffected == 0) {
            throw new RuntimeException("Book was not added.");
        }

        /*
         * Retrieve the ID automatically generated by MySQL.
         */
        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {

            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
        }

    } catch (SQLException e) {

        // Report the database error to the calling code.
        throw new RuntimeException("Could not add book.", e);
    }

    throw new RuntimeException("Book was added, but no ID was returned.");
}
}