import com.percy.library.dao.BookDAO;
import com.percy.library.dao.BorrowerDAO;
import com.percy.library.model.Book;
import com.percy.library.model.Borrower;

import java.util.List;

/**
 * Tests the search and sorting functionality of the
 * Library Management System.
 */
public class SearchAndSortingTest {

    public static void main(String[] args) {

        BookDAO bookDAO = new BookDAO();
        BorrowerDAO borrowerDAO = new BorrowerDAO();

        // ==========================================
        // SEARCH BOOKS BY TITLE
        // ==========================================

        System.out.println("===== SEARCH BOOKS BY TITLE =====");

        List<Book> titleResults =
                bookDAO.searchByTitle("java");

        System.out.println(
                "Books found: " + titleResults.size()
        );

        for (Book book : titleResults) {
            System.out.println(book);
        }

        // ==========================================
        // SEARCH BOOKS BY AUTHOR
        // ==========================================

        System.out.println("\n===== SEARCH BOOKS BY AUTHOR =====");

        List<Book> authorResults =
                bookDAO.searchByAuthor("James");

        System.out.println(
                "Books found: " + authorResults.size()
        );

        for (Book book : authorResults) {
            System.out.println(book);
        }

        // ==========================================
        // FILTER BOOKS BY GENRE
        // ==========================================

        System.out.println("\n===== FILTER BOOKS BY GENRE =====");

        List<Book> genreResults =
                bookDAO.filterByGenre("Programming");

        System.out.println(
                "Books found: " + genreResults.size()
        );

        for (Book book : genreResults) {
            System.out.println(book);
        }

        // ==========================================
        // SORT BOOKS BY TITLE
        // ==========================================

        System.out.println("\n===== SORT BOOKS BY TITLE =====");

        List<Book> titleSorted =
                bookDAO.getBooksSortedByTitle();

        for (Book book : titleSorted) {
            System.out.println(
                    book.getTitle()
            );
        }

        // ==========================================
        // SORT BOOKS BY PUBLICATION YEAR
        // ==========================================

        System.out.println(
                "\n===== SORT BOOKS BY PUBLICATION YEAR ====="
        );

        List<Book> yearSorted =
                bookDAO.getBooksSortedByPublicationYear();

        for (Book book : yearSorted) {
            System.out.println(
                    book.getPublicationYear()
                            + " - "
                            + book.getTitle()
            );
        }

        // ==========================================
        // SEARCH BORROWERS BY NAME
        // ==========================================

        System.out.println("\n===== SEARCH BORROWERS BY NAME =====");

        List<Borrower> nameResults =
                borrowerDAO.searchByName("Percy");

        System.out.println(
                "Borrowers found: " + nameResults.size()
        );

        for (Borrower borrower : nameResults) {
            System.out.println(borrower);
        }

        // ==========================================
        // SEARCH BORROWERS BY EMAIL
        // ==========================================

        System.out.println("\n===== SEARCH BORROWERS BY EMAIL =====");

        List<Borrower> emailResults =
                borrowerDAO.searchByEmail("john@example.com");

        System.out.println(
                "Borrowers found: " + emailResults.size()
        );

        for (Borrower borrower : emailResults) {
            System.out.println(borrower);
        }

        System.out.println(
                "\n===== SEARCH AND SORTING TEST COMPLETE ====="
        );
    }
}