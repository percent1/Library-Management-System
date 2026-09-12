package com.percy.library.model;

/**
 * Represents a borrower in the library.
 *
 * A borrower is a person who can borrow books
 * from the library.
 */
public class Borrower {

    /*
     * Unique identifier for the borrower.
     */
    private int borrowerId;

    /*
     * Borrower's first name.
     */
    private String firstName;

    /*
     * Borrower's last name.
     */
    private String lastName;

    /*
     * Borrower's email address.
     */
    private String email;

    /*
     * Borrower's telephone number.
     */
    private String phone;

    /**
     * Creates an empty Borrower object.
     */
    public Borrower() {
    }

    /**
     * Creates a Borrower object with all its information.
     *
     * @param borrowerId unique borrower identifier
     * @param firstName borrower's first name
     * @param lastName borrower's last name
     * @param email borrower's email address
     * @param phone borrower's phone number
     */
    public Borrower(int borrowerId, String firstName, String lastName,
                    String email, String phone) {

        this.borrowerId = borrowerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public int getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(int borrowerId) {
        this.borrowerId = borrowerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Returns a readable representation of the borrower.
     *
     * @return borrower information as a String
     */
    @Override
    public String toString() {
        return "Borrower{" +
                "borrowerId=" + borrowerId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}