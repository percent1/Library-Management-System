import com.percy.library.dao.BookDAO;
import com.percy.library.dao.LoanDAO;
import com.percy.library.model.Book;
import com.percy.library.model.Loan;
import com.percy.library.service.LibraryService;

/**
 * Tests the borrowing and returning functionality
 * of the Library Management System.
 */
public class BorrowReturnTest {

    public static void main(String[] args) {

        LibraryService libraryService = new LibraryService();

        BookDAO bookDAO = new BookDAO();
        LoanDAO loanDAO = new LoanDAO();

        System.out.println("===== INITIAL BOOK STATUS =====");

        Book book = bookDAO.getBookById(2);

        System.out.println(book);

        System.out.println();
        System.out.println("===== BORROW BOOK =====");

        int loanId = libraryService.borrowBook(2, 2);

        System.out.println("New loan ID: " + loanId);

        Book borrowedBook = bookDAO.getBookById(2);

        System.out.println("Book available: "
                + borrowedBook.isAvailable());

        Loan newLoan = loanDAO.getLoanById(loanId);

        System.out.println(newLoan);

        System.out.println();
        System.out.println("===== TRY TO BORROW SAME BOOK AGAIN =====");

        try {

            libraryService.borrowBook(2, 3);

            System.out.println(
                    "ERROR: Second borrow should have failed."
            );

        } catch (IllegalStateException e) {

            System.out.println(
                    "Expected error: " + e.getMessage()
            );
        }

        System.out.println();
        System.out.println("===== RETURN BOOK =====");

        libraryService.returnBook(loanId);

        Loan returnedLoan = loanDAO.getLoanById(loanId);
        Book returnedBook = bookDAO.getBookById(2);

        System.out.println(returnedLoan);

        System.out.println("Book available: "
                + returnedBook.isAvailable());

        System.out.println();
        System.out.println("===== TRY TO RETURN BOOK AGAIN =====");

        try {

            libraryService.returnBook(loanId);

            System.out.println(
                    "ERROR: Second return should have failed."
            );

        } catch (IllegalStateException e) {

            System.out.println(
                    "Expected error: " + e.getMessage()
            );
        }

        System.out.println();
        System.out.println("===== VERIFY ORIGINAL LOAN =====");

        Loan originalLoan = loanDAO.getLoanById(1);

        System.out.println(originalLoan);

        System.out.println();
        System.out.println("===== BORROW AND RETURN TEST COMPLETE =====");
    }
}