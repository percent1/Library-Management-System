import com.percy.library.model.Borrower;

/**
 * Tests the Borrower model class.
 */
public class BorrowerTest {

    public static void main(String[] args) {

        // Create a Borrower object using the constructor.
        Borrower borrower = new Borrower(
                1,
                "Percy",
                "Ngobeni",
                "percy@example.com",
                "0712345678"
        );

        // Display the borrower's information.
        System.out.println(borrower);

        // Display individual values using getter methods.
        System.out.println("Name: "
                + borrower.getFirstName() + " "
                + borrower.getLastName());

        System.out.println("Email: " + borrower.getEmail());
        System.out.println("Phone: " + borrower.getPhone());

        // Update the phone number using a setter.
        borrower.setPhone("0799999999");

        System.out.println("Updated phone: " + borrower.getPhone());
    }
}