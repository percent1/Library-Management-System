# Library Management System

A desktop-based Library Management System built with Java, JavaFX, MySQL, and JDBC. The application provides a graphical interface for managing books, borrowers, and library loans.

The project demonstrates practical Java development using object-oriented programming, database integration, CRUD operations, the DAO and service-layer patterns, transaction handling, and JavaFX user interface development.

---

## Features

### Book Management

* View all books
* Add new books
* Edit existing books
* Delete books
* Search books by title
* Search books by author
* Filter books by genre
* Sort books by title
* Sort books by publication year
* Track book availability

### Borrower Management

* View all borrowers
* Add new borrowers
* Edit borrower information
* Delete borrowers
* Search borrowers by name
* Search borrowers by email

### Loan Management

* View active and completed loans
* Borrow available books
* Return borrowed books
* Automatically update book availability
* Automatically calculate a 14-day due date
* Prevent a book from being borrowed when it is already on loan
* Prevent a loan from being returned more than once
* Display book titles and borrower names alongside loan information

### Database

* MySQL relational database
* JDBC database connectivity
* Foreign-key relationships between books, borrowers, and loans
* Prepared statements for database operations
* Transaction handling for borrowing and returning books

### User Interface

* JavaFX desktop interface
* Separate Books, Borrowers, and Loans sections
* Search and filtering controls
* Add, edit, delete, borrow, and return dialogs
* Error handling and user feedback

---

## Technologies Used

| Technology | Purpose                           |
| ---------- | --------------------------------- |
| Java 21    | Application development           |
| JavaFX 21  | Graphical user interface          |
| MySQL      | Relational database               |
| JDBC       | Database connectivity             |
| Maven      | Project and dependency management |
| Git        | Version control                   |
| GitHub     | Source code hosting               |

---

## Project Structure

```text
Library Management System/
├── .gitignore
├── README.md
├── pom.xml
└── src/
    └── main/
        ├── java/
        │   ├── BookDAOTest.java
        │   ├── BookTest.java
        │   ├── BorrowerTest.java
        │   ├── DatabaseConnectionTest.java
        │   ├── LoanTest.java
        │   ├── BorrowerDAOTest.java
        │   ├── LoanDAOTest.java
        │   ├── SearchAndSortingTest.java
        │   ├── BorrowReturnTest.java
        │   └── com/
        │       └── percy/
        │           └── library/
        │               ├── config/
        │               │   └── DatabaseConnection.java
        │               ├── dao/
        │               │   ├── BookDAO.java
        │               │   ├── BorrowerDAO.java
        │               │   └── LoanDAO.java
        │               ├── model/
        │               │   ├── Book.java
        │               │   ├── Borrower.java
        │               │   ├── Loan.java
        │               │   └── LoanDetails.java
        │               ├── service/
        │               │   └── LibraryService.java
        │               └── ui/
        │                   └── LibraryApplication.java
        └── resources/
            ├── db.properties
            └── db.properties.example
```

---

## Application Architecture

The application is separated into several layers to keep responsibilities organised.

### Model

The model classes represent the main entities in the system:

* `Book`
* `Borrower`
* `Loan`
* `LoanDetails`

### DAO

The Data Access Object layer handles communication with the MySQL database.

* `BookDAO`
* `BorrowerDAO`
* `LoanDAO`

The DAO classes are responsible for database operations such as creating, reading, updating, deleting, searching, and sorting records.

### Service

`LibraryService` contains business logic that involves multiple database operations.

For example, borrowing a book requires both:

1. Creating a loan.
2. Updating the book's availability.

These operations are handled together using a database transaction.

### UI

`LibraryApplication` provides the JavaFX graphical user interface through which users interact with the system.

---

## Database Design

The application uses three main tables.

### Books

Stores information about library books.

```text
book_id
title
author
isbn
genre
publication_year
available
```

### Borrowers

Stores information about people who borrow books.

```text
borrower_id
first_name
last_name
email
phone
```

### Loans

Stores borrowing and return information.

```text
loan_id
book_id
borrower_id
loan_date
due_date
return_date
```

The `loans` table uses foreign keys to connect books and borrowers.

```text
Books
  │
  └── book_id
          │
          ▼
       Loans
          ▲
          │
  ┌───────┘
  │
Borrowers
```

---

## Borrowing Process

When a user borrows a book, the application:

1. Checks that the book exists.
2. Checks that the book is available.
3. Checks that the borrower exists.
4. Creates a new loan.
5. Sets the loan date to the current date.
6. Calculates a 14-day due date.
7. Marks the book as unavailable.
8. Commits the database transaction.

If an error occurs, the transaction is rolled back to prevent inconsistent data.

---

## Returning a Book

When a user returns a book, the application:

1. Finds the loan.
2. Checks that the loan exists.
3. Checks that it has not already been returned.
4. Records the return date.
5. Marks the book as available.
6. Commits the transaction.

---

## Requirements

Before running the application, install:

* Java 21
* Maven
* MySQL

Verify Java and Maven:

```bash
java -version
mvn -version
```

---

## Database Setup

Create a MySQL database named:

```sql
CREATE DATABASE LibraryDB;
```

Create the required `books`, `borrowers`, and `loans` tables in the database.

The application uses a configuration file:

```text
src/main/resources/db.properties
```

Example:

```properties
db.url=jdbc:mysql://localhost:3306/LibraryDB
db.username=your_mysql_username
db.password=your_mysql_password
```

For security, `db.properties` is excluded from Git using `.gitignore`.

A template is provided as:

```text
src/main/resources/db.properties.example
```

Copy the example file and replace the placeholder database credentials with your own.

---

## Running the Application

Clone the repository:

```bash
git clone https://github.com/percent1/Library-Management-System.git
```

Navigate into the project:

```bash
cd "Library Management System"
```

Compile the project:

```bash
mvn clean compile
```

Run the JavaFX application:

```bash
mvn javafx:run
```

---

## Testing

The project includes tests for:

* Database connectivity
* Book model
* Borrower model
* Loan model
* Book DAO operations
* Borrower DAO operations
* Loan DAO operations
* Searching and sorting
* Borrowing and returning books

The borrowing and returning functionality was also tested for error scenarios, including:

* Attempting to borrow an unavailable book
* Attempting to return an already returned loan

---

## Error Handling

The application provides validation and error handling for common situations, including:

* Invalid database operations
* Missing books
* Missing borrowers
* Unavailable books
* Invalid loan IDs
* Attempting to return an already returned loan

Database resources are managed using Java's try-with-resources mechanism.

---

## Future Improvements

Possible future improvements include:

* User authentication and roles
* Fines for overdue books
* Dashboard with library statistics
* Book cover images
* Advanced reporting
* Exporting reports to PDF
* Pagination for large datasets
* Improved UI styling
* Automated unit testing with JUnit
* Packaging the application for easier distribution

---

## What I Learned

This project provided practical experience with:

* Object-oriented programming
* Java classes and encapsulation
* JDBC
* MySQL database design
* CRUD operations
* SQL queries
* Prepared statements
* DAO architecture
* Service-layer architecture
* Database transactions
* JavaFX application development
* Maven dependency management
* Exception handling
* Git and GitHub

---

## Author

**Percy Ngobeni**

Aspiring Junior Software Developer

GitHub:
https://github.com/percent1

---

## Project Status

**Completed**

The core library management functionality has been implemented, tested, and integrated into a JavaFX desktop application.
