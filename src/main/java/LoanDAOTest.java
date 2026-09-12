import com.percy.library.dao.LoanDAO;
import com.percy.library.model.Loan;

import java.time.LocalDate;
import java.util.List;

/**
 * Tests all CRUD operations provided by LoanDAO.
 */
public class LoanDAOTest {

    public static void main(String[] args) {

        LoanDAO loanDAO = new LoanDAO();

        // ==========================================
        // READ ALL LOANS
        // ==========================================

        System.out.println("===== READ ALL LOANS =====");

        List<Loan> loans = loanDAO.getAllLoans();

        System.out.println("Loans found: " + loans.size());

        for (Loan loan : loans) {
            System.out.println(loan);
        }

        // ==========================================
        // CREATE LOAN
        // ==========================================

        System.out.println("\n===== CREATE LOAN =====");

        Loan newLoan = new Loan(
                0,
                2,
                2,
                LocalDate.now(),
                LocalDate.now().plusDays(14),
                null
        );

        int generatedId = loanDAO.addLoan(newLoan);

        System.out.println("New loan ID: " + generatedId);

        // ==========================================
        // READ LOAN BY ID
        // ==========================================

        System.out.println("\n===== READ LOAN BY ID =====");

        Loan savedLoan =
                loanDAO.getLoanById(generatedId);

        System.out.println(savedLoan);

        // ==========================================
        // UPDATE LOAN
        // ==========================================

        System.out.println("\n===== UPDATE LOAN =====");

        Loan updatedLoan = new Loan(
                generatedId,
                2,
                2,
                LocalDate.now(),
                LocalDate.now().plusDays(21),
                LocalDate.now()
        );

        boolean updated =
                loanDAO.updateLoan(updatedLoan);

        System.out.println("Loan updated: " + updated);

        Loan afterUpdate =
                loanDAO.getLoanById(generatedId);

        System.out.println(afterUpdate);

        // ==========================================
        // DELETE LOAN
        // ==========================================

        System.out.println("\n===== DELETE LOAN =====");

        boolean deleted =
                loanDAO.deleteLoan(generatedId);

        System.out.println("Loan deleted: " + deleted);

        // ==========================================
        // VERIFY DELETE
        // ==========================================

        System.out.println("\n===== VERIFY DELETE =====");

        Loan afterDelete =
                loanDAO.getLoanById(generatedId);

        System.out.println(
                "Loan after deletion: " + afterDelete
        );
    }
}