package com.percy.library.model;

import java.time.LocalDate;

/**
 * Represents a loan transaction in the library.
 *
 * A loan records which book was borrowed,
 * which borrower borrowed it, and the relevant dates.
 */
public class Loan {

    /*
     * Unique identifier for the loan.
     */
    private int loanId;

    /*
     * ID of the borrowed book.
     */
    private int bookId;

    /*
     * ID of the borrower.
     */
    private int borrowerId;

    /*
     * Date on which the book was borrowed.
     */
    private LocalDate loanDate;

    /*
     * Date by which the book should be returned.
     */
    private LocalDate dueDate;

    /*
     * Date on which the book was actually returned.
     *
     * This can be null when the book has not yet been returned.
     */
    private LocalDate returnDate;

    /**
     * Creates an empty Loan object.
     */
    public Loan() {
    }

    /**
     * Creates a Loan object with all loan information.
     *
     * @param loanId unique loan identifier
     * @param bookId ID of the borrowed book
     * @param borrowerId ID of the borrower
     * @param loanDate date the book was borrowed
     * @param dueDate date the book is due
     * @param returnDate date the book was returned
     */
    public Loan(int loanId, int bookId, int borrowerId,
                LocalDate loanDate, LocalDate dueDate,
                LocalDate returnDate) {

        this.loanId = loanId;
        this.bookId = bookId;
        this.borrowerId = borrowerId;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
    }

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(int borrowerId) {
        this.borrowerId = borrowerId;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    /**
     * Returns a readable representation of the loan.
     *
     * @return loan information as a String
     */
    @Override
    public String toString() {
        return "Loan{" +
                "loanId=" + loanId +
                ", bookId=" + bookId +
                ", borrowerId=" + borrowerId +
                ", loanDate=" + loanDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                '}';
    }
}
