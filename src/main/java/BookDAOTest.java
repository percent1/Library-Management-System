import com.percy.library.dao.BookDAO;
import com.percy.library.model.Book;

/**
 * Tests the BookDAO database operations.
 */
public class BookDAOTest {

    public static void main(String[] args) {

        // Create a BookDAO object.
        BookDAO bookDAO = new BookDAO();

        // Delete the test book with ID 5.
        boolean deleted = bookDAO.deleteBook(5);

        // Display whether the deletion was successful.
        System.out.println("Book deleted: " + deleted);

        // Try to retrieve the deleted book.
        Book deletedBook = bookDAO.getBookById(5);

        // Display the result.
        System.out.println("Book with ID 5 after deletion:");
        System.out.println(deletedBook);
    }
}