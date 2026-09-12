import com.percy.library.model.Book;

/**
 * Tests the Book model class.
 */
public class BookTest {

    public static void main(String[] args) {

        // Create a Book object using the constructor.
        Book book = new Book(
                1,
                "Clean Code",
                "Robert C. Martin",
                "9780132350884",
                "Programming",
                2008,
                false
        );

        // Display the book's information.
        System.out.println(book);

        // Display individual values using getter methods.
        System.out.println("Title: " + book.getTitle());
        System.out.println("Author: " + book.getAuthor());
        System.out.println("Available: " + book.isAvailable());

        // Change the availability using a setter.
        book.setAvailable(true);

        System.out.println("Updated availability: " + book.isAvailable());
    }
}