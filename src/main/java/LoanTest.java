import com.percy.library.model.Loan;

import java.time.LocalDate;

/**
 * Tests the Loan model class.
 */
public class LoanTest {

    public static void main(String[] args) {

        // Create a loan that has not yet been returned.
        Loan loan = new Loan(
                1,
                1,
                1,
                LocalDate.of(2026, 9, 12),
                LocalDate.of(2026, 9, 26),
                null
        );

        // Display the loan information.
        System.out.println(loan);

        // Display individual values.
        System.out.println("Book ID: " + loan.getBookId());
        System.out.println("Borrower ID: " + loan.getBorrowerId());
        System.out.println("Loan Date: " + loan.getLoanDate());
        System.out.println("Due Date: " + loan.getDueDate());
        System.out.println("Return Date: " + loan.getReturnDate());

        // Simulate the book being returned.
        loan.setReturnDate(LocalDate.of(2026, 9, 20));

        System.out.println("Updated Return Date: "
                + loan.getReturnDate());
    }
}