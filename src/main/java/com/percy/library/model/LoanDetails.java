
package com.percy.library.model;

import java.time.LocalDate;

/**
 * Represents loan information together with the
 * book title and borrower name.
 *
 * This class is used by the user interface to display
 * more meaningful loan information.
 */
public class LoanDetails {

    private int loanId;
    private String bookTitle;
    private String borrowerName;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    public LoanDetails(
            int loanId,
            String bookTitle,
            String borrowerName,
            LocalDate loanDate,
            LocalDate dueDate,
            LocalDate returnDate) {

        this.loanId = loanId;
        this.bookTitle = bookTitle;
        this.borrowerName = borrowerName;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
    }

    public int getLoanId() {
        return loanId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }
}

