import com.percy.library.dao.BorrowerDAO;
import com.percy.library.model.Borrower;

import java.util.List;

/**
 * Tests all CRUD operations provided by BorrowerDAO.
 */
public class BorrowerDAOTest {

    public static void main(String[] args) {

        BorrowerDAO borrowerDAO = new BorrowerDAO();

        // ==========================================
        // READ ALL BORROWERS
        // ==========================================

        System.out.println("===== READ ALL BORROWERS =====");

        List<Borrower> borrowers = borrowerDAO.getAllBorrowers();

        System.out.println("Borrowers found: " + borrowers.size());

        for (Borrower borrower : borrowers) {
            System.out.println(borrower);
        }


        // ==========================================
        // CREATE BORROWER
        // ==========================================

        System.out.println("\n===== CREATE BORROWER =====");

        Borrower newBorrower = new Borrower(
                0,
                "Michael",
                "Johnson",
                "michael@example.com",
                "0745678901"
        );

        int generatedId = borrowerDAO.addBorrower(newBorrower);

        System.out.println("New borrower ID: " + generatedId);


        // ==========================================
        // READ BORROWER BY ID
        // ==========================================

        System.out.println("\n===== READ BORROWER BY ID =====");

        Borrower savedBorrower =
                borrowerDAO.getBorrowerById(generatedId);

        System.out.println(savedBorrower);


        // ==========================================
        // UPDATE BORROWER
        // ==========================================

        System.out.println("\n===== UPDATE BORROWER =====");

        Borrower updatedBorrower = new Borrower(
                generatedId,
                "Michael",
                "Johnson",
                "michael.johnson@example.com",
                "0798765432"
        );

        boolean updated =
                borrowerDAO.updateBorrower(updatedBorrower);

        System.out.println("Borrower updated: " + updated);

        Borrower afterUpdate =
                borrowerDAO.getBorrowerById(generatedId);

        System.out.println(afterUpdate);


        // ==========================================
        // DELETE BORROWER
        // ==========================================

        System.out.println("\n===== DELETE BORROWER =====");

        boolean deleted =
                borrowerDAO.deleteBorrower(generatedId);

        System.out.println("Borrower deleted: " + deleted);


        // ==========================================
        // VERIFY DELETE
        // ==========================================

        System.out.println("\n===== VERIFY DELETE =====");

        Borrower afterDelete =
                borrowerDAO.getBorrowerById(generatedId);

        System.out.println(
                "Borrower after deletion: " + afterDelete
        );
    }
}