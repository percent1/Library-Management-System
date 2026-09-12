
package com.percy.library.ui;

import com.percy.library.dao.BookDAO;
import com.percy.library.dao.BorrowerDAO;
import com.percy.library.dao.LoanDAO;
import com.percy.library.model.Book;
import com.percy.library.model.Borrower;
import com.percy.library.model.Loan;
import com.percy.library.model.LoanDetails;
import com.percy.library.service.LibraryService;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

/**
 * Main JavaFX application for the Library Management System.
 *
 * The application provides screens for managing:
 * - Books
 * - Borrowers
 * - Loans
 *
 * The user interface communicates with the DAO and service
 * layers instead of accessing the database directly.
 */
public class LibraryApplication extends Application {

    private final BookDAO bookDAO = new BookDAO();
    private final BorrowerDAO borrowerDAO = new BorrowerDAO();
    private final LoanDAO loanDAO = new LoanDAO();
    private final LibraryService libraryService = new LibraryService();

    private final BorderPane root = new BorderPane();

    // ============================================================
    // BOOKS
    // ============================================================

    private final TableView<Book> bookTable =
            new TableView<>();

    private final ObservableList<Book> books =
            FXCollections.observableArrayList();

    private final TextField bookSearchField =
            new TextField();

    private final ComboBox<String> bookSearchType =
            new ComboBox<>();

    private final ComboBox<String> genreFilter =
            new ComboBox<>();

    // ============================================================
    // BORROWERS
    // ============================================================

    private final TableView<Borrower> borrowerTable =
            new TableView<>();

    private final ObservableList<Borrower> borrowers =
            FXCollections.observableArrayList();

    private final TextField borrowerSearchField =
            new TextField();

    private final ComboBox<String> borrowerSearchType =
            new ComboBox<>();

    // ============================================================
    // LOANS
    // ============================================================

    /*
     * The Loans table uses LoanDetails because the UI needs
     * additional information such as the book title and
     * borrower name.
     */
    private final TableView<LoanDetails> loanTable =
            new TableView<>();

    private final ObservableList<LoanDetails> loanDetails =
            FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {

        stage.setTitle(
                "Library Management System"
        );

        root.setLeft(
                createNavigation()
        );

        root.setCenter(
                createBooksView()
        );

        Scene scene =
                new Scene(
                        root,
                        1150,
                        700
                );

        stage.setScene(scene);
        stage.show();

        loadBooks();
    }

    // ============================================================
    // NAVIGATION
    // ============================================================

    /**
     * Creates the navigation panel.
     */
    private VBox createNavigation() {

        Label title =
                new Label("LIBRARY");

        title.setStyle(
                "-fx-font-size: 22px; -fx-font-weight: bold;"
        );

        Button booksButton =
                new Button("Books");

        Button borrowersButton =
                new Button("Borrowers");

        Button loansButton =
                new Button("Loans");

        booksButton.setMaxWidth(
                Double.MAX_VALUE
        );

        borrowersButton.setMaxWidth(
                Double.MAX_VALUE
        );

        loansButton.setMaxWidth(
                Double.MAX_VALUE
        );

        booksButton.setOnAction(
                event -> root.setCenter(
                        createBooksView()
                )
        );

        borrowersButton.setOnAction(
                event -> root.setCenter(
                        createBorrowersView()
                )
        );

        loansButton.setOnAction(
                event -> root.setCenter(
                        createLoansView()
                )
        );

        VBox navigation =
                new VBox(
                        15,
                        title,
                        booksButton,
                        borrowersButton,
                        loansButton
                );

        navigation.setPadding(
                new Insets(20)
        );

        navigation.setPrefWidth(
                180
        );

        return navigation;
    }

    // ============================================================
    // BOOKS SCREEN
    // ============================================================

    /**
     * Creates the Books screen.
     */
    private VBox createBooksView() {

        Label heading =
                new Label("Books");

        heading.setStyle(
                "-fx-font-size: 28px; -fx-font-weight: bold;"
        );

        bookSearchType.getItems().clear();

        bookSearchType.getItems().addAll(
                "Title",
                "Author"
        );

        bookSearchType.setValue(
                "Title"
        );

        bookSearchField.setPromptText(
                "Search books..."
        );

        Button searchButton =
                new Button("Search");

        Button showAllButton =
                new Button("Show All");

        Button addButton =
                new Button("Add Book");

        Button editButton =
                new Button("Edit");

        Button deleteButton =
                new Button("Delete");

        Button refreshButton =
                new Button("Refresh");

        searchButton.setOnAction(
                event -> searchBooks()
        );

        showAllButton.setOnAction(
                event -> loadBooks()
        );

        addButton.setOnAction(
                event -> showAddBookDialog()
        );

        editButton.setOnAction(
                event -> showEditBookDialog()
        );

        deleteButton.setOnAction(
                event -> deleteSelectedBook()
        );

        refreshButton.setOnAction(
                event -> loadBooks()
        );

        HBox searchBar =
                new HBox(
                        10,
                        bookSearchType,
                        bookSearchField,
                        searchButton,
                        showAllButton
                );

        HBox.setHgrow(
                bookSearchField,
                Priority.ALWAYS
        );

        genreFilter.getItems().clear();

        genreFilter.getItems().addAll(
                "All Genres",
                "Programming",
                "Self-Help"
        );

        genreFilter.setValue(
                "All Genres"
        );

        Button genreButton =
                new Button("Filter Genre");

        genreButton.setOnAction(
                event -> filterGenre()
        );

        HBox actions =
                new HBox(
                        10,
                        addButton,
                        editButton,
                        deleteButton,
                        refreshButton,
                        genreFilter,
                        genreButton
                );

        createBookTable();

        VBox content =
                new VBox(
                        20,
                        heading,
                        searchBar,
                        actions,
                        bookTable
                );

        content.setPadding(
                new Insets(25)
        );

        VBox.setVgrow(
                bookTable,
                Priority.ALWAYS
        );

        loadBooks();

        return content;
    }

    /**
     * Creates the Books table.
     */
    private void createBookTable() {

        bookTable.getColumns().clear();

        TableColumn<Book, String> idColumn =
                new TableColumn<>("ID");

        idColumn.setCellValueFactory(
                data -> new SimpleStringProperty(
                        String.valueOf(
                                data.getValue()
                                        .getBookId()
                        )
                )
        );

        TableColumn<Book, String> titleColumn =
                new TableColumn<>("Title");

        titleColumn.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue()
                                .getTitle()
                )
        );

        TableColumn<Book, String> authorColumn =
                new TableColumn<>("Author");

        authorColumn.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue()
                                .getAuthor()
                )
        );

        TableColumn<Book, String> isbnColumn =
                new TableColumn<>("ISBN");

        isbnColumn.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue()
                                .getIsbn()
                )
        );

        TableColumn<Book, String> genreColumn =
                new TableColumn<>("Genre");

        genreColumn.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue()
                                .getGenre()
                )
        );

        TableColumn<Book, String> yearColumn =
                new TableColumn<>("Year");

        yearColumn.setCellValueFactory(
                data -> new SimpleStringProperty(
                        String.valueOf(
                                data.getValue()
                                        .getPublicationYear()
                        )
                )
        );

        TableColumn<Book, String> availableColumn =
                new TableColumn<>("Available");

        availableColumn.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue().isAvailable()
                                ? "Yes"
                                : "No"
                )
        );

        bookTable.getColumns().addAll(
                idColumn,
                titleColumn,
                authorColumn,
                isbnColumn,
                genreColumn,
                yearColumn,
                availableColumn
        );

        bookTable.setItems(
                books
        );

        bookTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN
        );
    }

    /**
     * Loads all books.
     */
    private void loadBooks() {

        try {

            List<Book> result =
                    bookDAO.getAllBooks();

            books.setAll(
                    result
            );

        } catch (Exception e) {

            showError(
                    "Could not load books.",
                    e.getMessage()
            );
        }
    }

    /**
     * Searches books.
     */
    private void searchBooks() {

        String searchText =
                bookSearchField
                        .getText()
                        .trim();

        if (searchText.isEmpty()) {

            loadBooks();

            return;
        }

        try {

            List<Book> result;

            if (bookSearchType
                    .getValue()
                    .equals("Title")) {

                result =
                        bookDAO.searchByTitle(
                                searchText
                        );

            } else {

                result =
                        bookDAO.searchByAuthor(
                                searchText
                        );
            }

            books.setAll(
                    result
            );

        } catch (Exception e) {

            showError(
                    "Search failed.",
                    e.getMessage()
            );
        }
    }

    /**
     * Filters books by genre.
     */
    private void filterGenre() {

        String genre =
                genreFilter.getValue();

        if (genre.equals("All Genres")) {

            loadBooks();

            return;
        }

        try {

            books.setAll(
                    bookDAO.filterByGenre(
                            genre
                    )
            );

        } catch (Exception e) {

            showError(
                    "Genre filtering failed.",
                    e.getMessage()
            );
        }
    }

    /**
     * Opens the Add Book dialog.
     */
    private void showAddBookDialog() {

        Dialog<ButtonType> dialog =
                new Dialog<>();

        dialog.setTitle(
                "Add Book"
        );

        dialog.setHeaderText(
                "Add a new book"
        );

        GridPane form =
                createBookForm();

        dialog.getDialogPane()
                .setContent(form);

        ButtonType saveButton =
                new ButtonType(
                        "Save",
                        ButtonBar.ButtonData.OK_DONE
                );

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        saveButton,
                        ButtonType.CANCEL
                );

        dialog.showAndWait()
                .ifPresent(result -> {

                    if (result == saveButton) {

                        try {

                            Book book =
                                    getBookFromForm(
                                            form
                                    );

                            bookDAO.addBook(
                                    book
                            );

                            loadBooks();

                        } catch (Exception e) {

                            showError(
                                    "Could not add book.",
                                    e.getMessage()
                            );
                        }
                    }
                });
    }

    /**
     * Opens the Edit Book dialog.
     */
    private void showEditBookDialog() {

        Book selectedBook =
                bookTable
                        .getSelectionModel()
                        .getSelectedItem();

        if (selectedBook == null) {

            showError(
                    "No book selected.",
                    "Please select a book to edit."
            );

            return;
        }

        Dialog<ButtonType> dialog =
                new Dialog<>();

        dialog.setTitle(
                "Edit Book"
        );

        dialog.setHeaderText(
                "Edit book details"
        );

        GridPane form =
                createBookForm();

        populateBookForm(
                form,
                selectedBook
        );

        dialog.getDialogPane()
                .setContent(form);

        ButtonType saveButton =
                new ButtonType(
                        "Save",
                        ButtonBar.ButtonData.OK_DONE
                );

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        saveButton,
                        ButtonType.CANCEL
                );

        dialog.showAndWait()
                .ifPresent(result -> {

                    if (result == saveButton) {

                        try {

                            Book updatedBook =
                                    getBookFromForm(
                                            form
                                    );

                            updatedBook.setBookId(
                                    selectedBook
                                            .getBookId()
                            );

                            updatedBook.setAvailable(
                                    selectedBook.isAvailable()
                            );

                            bookDAO.updateBook(
                                    updatedBook
                            );

                            loadBooks();

                        } catch (Exception e) {

                            showError(
                                    "Could not update book.",
                                    e.getMessage()
                            );
                        }
                    }
                });
    }

    /**
     * Deletes the selected book.
     */
    private void deleteSelectedBook() {

        Book selectedBook =
                bookTable
                        .getSelectionModel()
                        .getSelectedItem();

        if (selectedBook == null) {

            showError(
                    "No book selected.",
                    "Please select a book to delete."
            );

            return;
        }

        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmation.setTitle(
                "Delete Book"
        );

        confirmation.setHeaderText(
                "Delete \"" +
                        selectedBook.getTitle() +
                        "\"?"
        );

        confirmation.setContentText(
                "This action cannot be undone."
        );

        confirmation.showAndWait()
                .ifPresent(result -> {

                    if (result == ButtonType.OK) {

                        try {

                            bookDAO.deleteBook(
                                    selectedBook
                                            .getBookId()
                            );

                            loadBooks();

                        } catch (Exception e) {

                            showError(
                                    "Could not delete book.",
                                    e.getMessage()
                            );
                        }
                    }
                });
    }

    /**
     * Creates the Book form.
     */
    private GridPane createBookForm() {

        GridPane form =
                new GridPane();

        form.setHgap(10);
        form.setVgap(10);

        form.setPadding(
                new Insets(20)
        );

        form.add(
                new Label("Title:"),
                0,
                0
        );

        form.add(
                new Label("Author:"),
                0,
                1
        );

        form.add(
                new Label("ISBN:"),
                0,
                2
        );

        form.add(
                new Label("Genre:"),
                0,
                3
        );

        form.add(
                new Label("Publication Year:"),
                0,
                4
        );

        form.add(
                new TextField(),
                1,
                0
        );

        form.add(
                new TextField(),
                1,
                1
        );

        form.add(
                new TextField(),
                1,
                2
        );

        form.add(
                new TextField(),
                1,
                3
        );

        form.add(
                new TextField(),
                1,
                4
        );

        return form;
    }

    /**
     * Creates a Book object from the form.
     *
     * The fields are located using their GridPane coordinates
     * instead of relying on child indexes.
     */
    private Book getBookFromForm(
            GridPane form
    ) {

        TextField title =
                (TextField) getNodeFromGridPane(
                        form,
                        1,
                        0
                );

        TextField author =
                (TextField) getNodeFromGridPane(
                        form,
                        1,
                        1
                );

        TextField isbn =
                (TextField) getNodeFromGridPane(
                        form,
                        1,
                        2
                );

        TextField genre =
                (TextField) getNodeFromGridPane(
                        form,
                        1,
                        3
                );

        TextField year =
                (TextField) getNodeFromGridPane(
                        form,
                        1,
                        4
                );

        return new Book(
                0,
                title.getText(),
                author.getText(),
                isbn.getText(),
                genre.getText(),
                Integer.parseInt(
                        year.getText()
                ),
                true
        );
    }

    /**
     * Populates the Book edit form.
     */
    private void populateBookForm(
            GridPane form,
            Book book
    ) {

        TextField title =
                (TextField) getNodeFromGridPane(
                        form,
                        1,
                        0
                );

        TextField author =
                (TextField) getNodeFromGridPane(
                        form,
                        1,
                        1
                );

        TextField isbn =
                (TextField) getNodeFromGridPane(
                        form,
                        1,
                        2
                );

        TextField genre =
                (TextField) getNodeFromGridPane(
                        form,
                        1,
                        3
                );

        TextField year =
                (TextField) getNodeFromGridPane(
                        form,
                        1,
                        4
                );

        title.setText(
                book.getTitle()
        );

        author.setText(
                book.getAuthor()
        );

        isbn.setText(
                book.getIsbn()
        );

        genre.setText(
                book.getGenre()
        );

        if (book.getPublicationYear() != null) {

            year.setText(
                    String.valueOf(
                            book.getPublicationYear()
                    )
            );
        }
    }

    // ============================================================
    // BORROWERS SCREEN
    // ============================================================

    /**
     * Creates the Borrowers screen.
     */
    private VBox createBorrowersView() {

        Label heading =
                new Label("Borrowers");

        heading.setStyle(
                "-fx-font-size: 28px; -fx-font-weight: bold;"
        );

        borrowerSearchType
                .getItems()
                .clear();

        borrowerSearchType
                .getItems()
                .addAll(
                        "Name",
                        "Email"
                );

        borrowerSearchType.setValue(
                "Name"
        );

        borrowerSearchField.setPromptText(
                "Search borrowers..."
        );

        Button searchButton =
                new Button("Search");

        Button showAllButton =
                new Button("Show All");

        Button addButton =
                new Button("Add Borrower");

        Button editButton =
                new Button("Edit");

        Button deleteButton =
                new Button("Delete");

        Button refreshButton =
                new Button("Refresh");

        searchButton.setOnAction(
                event -> searchBorrowers()
        );

        showAllButton.setOnAction(
                event -> loadBorrowers()
        );

        addButton.setOnAction(
                event -> showAddBorrowerDialog()
        );

        editButton.setOnAction(
                event -> showEditBorrowerDialog()
        );

        deleteButton.setOnAction(
                event -> deleteSelectedBorrower()
        );

        refreshButton.setOnAction(
                event -> loadBorrowers()
        );

        HBox searchBar =
                new HBox(
                        10,
                        borrowerSearchType,
                        borrowerSearchField,
                        searchButton,
                        showAllButton
                );

        HBox.setHgrow(
                borrowerSearchField,
                Priority.ALWAYS
        );

        HBox actions =
                new HBox(
                        10,
                        addButton,
                        editButton,
                        deleteButton,
                        refreshButton
                );

        createBorrowerTable();

        VBox content =
                new VBox(
                        20,
                        heading,
                        searchBar,
                        actions,
                        borrowerTable
                );

        content.setPadding(
                new Insets(25)
        );

        VBox.setVgrow(
                borrowerTable,
                Priority.ALWAYS
        );

        loadBorrowers();

        return content;
    }

    /**
     * Creates the Borrowers table.
     */
    private void createBorrowerTable() {

        borrowerTable
                .getColumns()
                .clear();

        TableColumn<Borrower, String> idColumn =
                new TableColumn<>("ID");

        idColumn.setCellValueFactory(
                data -> new SimpleStringProperty(
                        String.valueOf(
                                data.getValue()
                                        .getBorrowerId()
                        )
                )
        );

        TableColumn<Borrower, String> firstNameColumn =
                new TableColumn<>("First Name");

        firstNameColumn.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue()
                                .getFirstName()
                )
        );

        TableColumn<Borrower, String> lastNameColumn =
                new TableColumn<>("Last Name");

        lastNameColumn.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue()
                                .getLastName()
                )
        );

        TableColumn<Borrower, String> emailColumn =
                new TableColumn<>("Email");

        emailColumn.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue()
                                .getEmail()
                )
        );

        TableColumn<Borrower, String> phoneColumn =
                new TableColumn<>("Phone");

        phoneColumn.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue()
                                .getPhone()
                )
        );

        borrowerTable
                .getColumns()
                .addAll(
                        idColumn,
                        firstNameColumn,
                        lastNameColumn,
                        emailColumn,
                        phoneColumn
                );

        borrowerTable.setItems(
                borrowers
        );

        borrowerTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN
        );
    }

    /**
     * Loads all borrowers.
     */
    private void loadBorrowers() {

        try {

            borrowers.setAll(
                    borrowerDAO.getAllBorrowers()
            );

        } catch (Exception e) {

            showError(
                    "Could not load borrowers.",
                    e.getMessage()
            );
        }
    }

    /**
     * Searches borrowers.
     */
    private void searchBorrowers() {

        String searchText =
                borrowerSearchField
                        .getText()
                        .trim();

        if (searchText.isEmpty()) {

            loadBorrowers();

            return;
        }

        try {

            if (borrowerSearchType
                    .getValue()
                    .equals("Name")) {

                borrowers.setAll(
                        borrowerDAO.searchByName(
                                searchText
                        )
                );

            } else {

                borrowers.setAll(
                        borrowerDAO.searchByEmail(
                                searchText
                        )
                );
            }

        } catch (Exception e) {

            showError(
                    "Search failed.",
                    e.getMessage()
            );
        }
    }

    /**
     * Opens the Add Borrower dialog.
     */
    private void showAddBorrowerDialog() {

        Dialog<ButtonType> dialog =
                new Dialog<>();

        dialog.setTitle(
                "Add Borrower"
        );

        dialog.setHeaderText(
                "Add a new borrower"
        );

        GridPane form =
                createBorrowerForm();

        dialog.getDialogPane()
                .setContent(form);

        ButtonType saveButton =
                new ButtonType(
                        "Save",
                        ButtonBar.ButtonData.OK_DONE
                );

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        saveButton,
                        ButtonType.CANCEL
                );

        dialog.showAndWait()
                .ifPresent(result -> {

                    if (result == saveButton) {

                        try {

                            borrowerDAO.addBorrower(
                                    getBorrowerFromForm(
                                            form
                                    )
                            );

                            loadBorrowers();

                        } catch (Exception e) {

                            showError(
                                    "Could not add borrower.",
                                    e.getMessage()
                            );
                        }
                    }
                });
    }

    /**
     * Opens the Edit Borrower dialog.
     */
    private void showEditBorrowerDialog() {

        Borrower selected =
                borrowerTable
                        .getSelectionModel()
                        .getSelectedItem();

        if (selected == null) {

            showError(
                    "No borrower selected.",
                    "Please select a borrower to edit."
            );

            return;
        }

        Dialog<ButtonType> dialog =
                new Dialog<>();

        dialog.setTitle(
                "Edit Borrower"
        );

        dialog.setHeaderText(
                "Edit borrower details"
        );

        GridPane form =
                createBorrowerForm();

        populateBorrowerForm(
                form,
                selected
        );

        dialog.getDialogPane()
                .setContent(form);

        ButtonType saveButton =
                new ButtonType(
                        "Save",
                        ButtonBar.ButtonData.OK_DONE
                );

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        saveButton,
                        ButtonType.CANCEL
                );

        dialog.showAndWait()
                .ifPresent(result -> {

                    if (result == saveButton) {

                        try {

                            Borrower updated =
                                    getBorrowerFromForm(
                                            form
                                    );

                            updated.setBorrowerId(
                                    selected
                                            .getBorrowerId()
                            );

                            borrowerDAO.updateBorrower(
                                    updated
                            );

                            loadBorrowers();

                        } catch (Exception e) {

                            showError(
                                    "Could not update borrower.",
                                    e.getMessage()
                            );
                        }
                    }
                });
    }

    /**
     * Deletes the selected borrower.
     */
    private void deleteSelectedBorrower() {

        Borrower selected =
                borrowerTable
                        .getSelectionModel()
                        .getSelectedItem();

        if (selected == null) {

            showError(
                    "No borrower selected.",
                    "Please select a borrower to delete."
            );

            return;
        }

        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmation.setTitle(
                "Delete Borrower"
        );

        confirmation.setHeaderText(
                "Delete " +
                        selected.getFirstName() +
                        " " +
                        selected.getLastName() +
                        "?"
        );

        confirmation.setContentText(
                "This action cannot be undone."
        );

        confirmation.showAndWait()
                .ifPresent(result -> {

                    if (result == ButtonType.OK) {

                        try {

                            borrowerDAO.deleteBorrower(
                                    selected
                                            .getBorrowerId()
                            );

                            loadBorrowers();

                        } catch (Exception e) {

                            showError(
                                    "Could not delete borrower.",
                                    e.getMessage()
                            );
                        }
                    }
                });
    }

    /**
     * Creates the Borrower form.
     */
    private GridPane createBorrowerForm() {

        GridPane form =
                new GridPane();

        form.setHgap(10);
        form.setVgap(10);

        form.setPadding(
                new Insets(20)
        );

        form.add(
                new Label("First Name:"),
                0,
                0
        );

        form.add(
                new Label("Last Name:"),
                0,
                1
        );

        form.add(
                new Label("Email:"),
                0,
                2
        );

        form.add(
                new Label("Phone:"),
                0,
                3
        );

        form.add(
                new TextField(),
                1,
                0
        );

        form.add(
                new TextField(),
                1,
                1
        );

        form.add(
                new TextField(),
                1,
                2
        );

        form.add(
                new TextField(),
                1,
                3
        );

        return form;
    }

    /**
     * Creates a Borrower object from the form.
     */
    private Borrower getBorrowerFromForm(
            GridPane form
    ) {

        TextField firstName =
                (TextField) getNodeFromGridPane(
                        form,
                        1,
                        0
                );

        TextField lastName =
                (TextField) getNodeFromGridPane(
                        form,
                        1,
                        1
                );

        TextField email =
                (TextField) getNodeFromGridPane(
                        form,
                        1,
                        2
                );

        TextField phone =
                (TextField) getNodeFromGridPane(
                        form,
                        1,
                        3
                );

        return new Borrower(
                0,
                firstName.getText(),
                lastName.getText(),
                email.getText(),
                phone.getText()
        );
    }

    /**
     * Populates the Borrower edit form.
     */
    private void populateBorrowerForm(
            GridPane form,
            Borrower borrower
    ) {

        TextField firstName =
                (TextField) getNodeFromGridPane(
                        form,
                        1,
                        0
                );

        TextField lastName =
                (TextField) getNodeFromGridPane(
                        form,
                        1,
                        1
                );

        TextField email =
                (TextField) getNodeFromGridPane(
                        form,
                        1,
                        2
                );

        TextField phone =
                (TextField) getNodeFromGridPane(
                        form,
                        1,
                        3
                );

        firstName.setText(
                borrower.getFirstName()
        );

        lastName.setText(
                borrower.getLastName()
        );

        email.setText(
                borrower.getEmail()
        );

        phone.setText(
                borrower.getPhone()
        );
    }

    // ============================================================
    // GRIDPANE HELPER
    // ============================================================

    /**
     * Finds a component inside a GridPane using its
     * column and row coordinates.
     *
     * This prevents ClassCastException errors caused by
     * relying on the order of children in getChildren().
     */
    private javafx.scene.Node getNodeFromGridPane(
            GridPane gridPane,
            int column,
            int row
    ) {

        for (javafx.scene.Node node :
                gridPane.getChildren()) {

            Integer nodeColumn =
                    GridPane.getColumnIndex(node);

            Integer nodeRow =
                    GridPane.getRowIndex(node);

            int actualColumn =
                    nodeColumn == null
                            ? 0
                            : nodeColumn;

            int actualRow =
                    nodeRow == null
                            ? 0
                            : nodeRow;

            if (actualColumn == column &&
                    actualRow == row) {

                return node;
            }
        }

        throw new IllegalArgumentException(
                "No component found at column "
                        + column
                        + ", row "
                        + row
        );
    }

    // ============================================================
    // LOANS SCREEN
    // ============================================================

    /**
     * Creates the Loans screen.
     */
    private VBox createLoansView() {

        Label heading =
                new Label("Loans");

        heading.setStyle(
                "-fx-font-size: 28px; -fx-font-weight: bold;"
        );

        Button borrowButton =
                new Button("Borrow Book");

        Button returnButton =
                new Button("Return Book");

        Button refreshButton =
                new Button("Refresh");

        borrowButton.setOnAction(
                event -> showBorrowBookDialog()
        );

        returnButton.setOnAction(
                event -> returnSelectedBook()
        );

        refreshButton.setOnAction(
                event -> loadLoans()
        );

        HBox actions =
                new HBox(
                        10,
                        borrowButton,
                        returnButton,
                        refreshButton
                );

        createLoanTable();

        VBox content =
                new VBox(
                        20,
                        heading,
                        actions,
                        loanTable
                );

        content.setPadding(
                new Insets(25)
        );

        VBox.setVgrow(
                loanTable,
                Priority.ALWAYS
        );

        loadLoans();

        return content;
    }

    /**
     * Creates the Loans table.
     *
     * LoanDetails is used so the table can display
     * meaningful book and borrower information.
     */
    private void createLoanTable() {

        loanTable
                .getColumns()
                .clear();

        TableColumn<LoanDetails, String> idColumn =
                new TableColumn<>("Loan ID");

        idColumn.setCellValueFactory(
                data -> new SimpleStringProperty(
                        String.valueOf(
                                data.getValue()
                                        .getLoanId()
                        )
                )
        );

        TableColumn<LoanDetails, String> bookColumn =
                new TableColumn<>("Book");

        bookColumn.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue()
                                .getBookTitle()
                )
        );

        TableColumn<LoanDetails, String> borrowerColumn =
                new TableColumn<>("Borrower");

        borrowerColumn.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue()
                                .getBorrowerName()
                )
        );

        TableColumn<LoanDetails, String> loanDateColumn =
                new TableColumn<>("Loan Date");

        loanDateColumn.setCellValueFactory(
                data -> new SimpleStringProperty(
                        String.valueOf(
                                data.getValue()
                                        .getLoanDate()
                        )
                )
        );

        TableColumn<LoanDetails, String> dueDateColumn =
                new TableColumn<>("Due Date");

        dueDateColumn.setCellValueFactory(
                data -> new SimpleStringProperty(
                        String.valueOf(
                                data.getValue()
                                        .getDueDate()
                        )
                )
        );

        TableColumn<LoanDetails, String> returnDateColumn =
                new TableColumn<>("Return Date");

        returnDateColumn.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue()
                                        .getReturnDate()
                                == null
                                ? "Not returned"
                                : String.valueOf(
                                        data.getValue()
                                                .getReturnDate()
                                )
                )
        );

        loanTable
                .getColumns()
                .addAll(
                        idColumn,
                        bookColumn,
                        borrowerColumn,
                        loanDateColumn,
                        dueDateColumn,
                        returnDateColumn
                );

        loanTable.setItems(
                loanDetails
        );

        loanTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN
        );
    }

    /**
     * Loads all loans together with book and borrower details.
     */
    private void loadLoans() {

        try {

            loanDetails.setAll(
                    loanDAO.getAllLoanDetails()
            );

        } catch (Exception e) {

            showError(
                    "Could not load loans.",
                    e.getMessage()
            );
        }
    }

    /**
     * Opens the Borrow Book dialog.
     */
    private void showBorrowBookDialog() {

        Dialog<ButtonType> dialog =
                new Dialog<>();

        dialog.setTitle(
                "Borrow Book"
        );

        dialog.setHeaderText(
                "Create a new loan"
        );

        ComboBox<Book> bookCombo =
                new ComboBox<>();

        ComboBox<Borrower> borrowerCombo =
                new ComboBox<>();

        try {

            bookCombo.getItems().addAll(
                    bookDAO.getAllBooks()
            );

            borrowerCombo.getItems().addAll(
                    borrowerDAO.getAllBorrowers()
            );

        } catch (Exception e) {

            showError(
                    "Could not load data.",
                    e.getMessage()
            );

            return;
        }

        bookCombo.setPromptText(
                "Select a book"
        );

        borrowerCombo.setPromptText(
                "Select a borrower"
        );

        GridPane form =
                new GridPane();

        form.setHgap(10);
        form.setVgap(10);

        form.setPadding(
                new Insets(20)
        );

        form.add(
                new Label("Book:"),
                0,
                0
        );

        form.add(
                bookCombo,
                1,
                0
        );

        form.add(
                new Label("Borrower:"),
                0,
                1
        );

        form.add(
                borrowerCombo,
                1,
                1
        );

        dialog.getDialogPane()
                .setContent(form);

        ButtonType borrowButton =
                new ButtonType(
                        "Borrow",
                        ButtonBar.ButtonData.OK_DONE
                );

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        borrowButton,
                        ButtonType.CANCEL
                );

        dialog.showAndWait()
                .ifPresent(result -> {

                    if (result == borrowButton) {

                        Book selectedBook =
                                bookCombo.getValue();

                        Borrower selectedBorrower =
                                borrowerCombo.getValue();

                        if (selectedBook == null ||
                                selectedBorrower == null) {

                            showError(
                                    "Missing information.",
                                    "Please select both a book and a borrower."
                            );

                            return;
                        }

                        try {

                            libraryService.borrowBook(
                                    selectedBook.getBookId(),
                                    selectedBorrower.getBorrowerId()
                            );

                            loadLoans();

                            showInformation(
                                    "Book Borrowed",
                                    "The book was borrowed successfully."
                            );

                        } catch (Exception e) {

                            showError(
                                    "Could not borrow book.",
                                    e.getMessage()
                            );
                        }
                    }
                });
    }

    /**
     * Returns the selected loan.
     *
     * The table contains LoanDetails for display, so the
     * original Loan is retrieved using its loan ID before
     * passing it to the service layer.
     */
    private void returnSelectedBook() {

        LoanDetails selectedLoanDetails =
                loanTable
                        .getSelectionModel()
                        .getSelectedItem();

        if (selectedLoanDetails == null) {

            showError(
                    "No loan selected.",
                    "Please select a loan to return."
            );

            return;
        }

        try {

            Loan selectedLoan =
                    loanDAO.getLoanById(
                            selectedLoanDetails.getLoanId()
                    );

            if (selectedLoan == null) {

                showError(
                        "Loan not found.",
                        "The selected loan could not be found."
                );

                return;
            }

            if (selectedLoan.getReturnDate() != null) {

                showError(
                        "Book already returned.",
                        "This loan has already been returned."
                );

                return;
            }

            Alert confirmation =
                    new Alert(
                            Alert.AlertType.CONFIRMATION
                    );

            confirmation.setTitle(
                    "Return Book"
            );

            confirmation.setHeaderText(
                    "Return \"" +
                            selectedLoanDetails.getBookTitle() +
                            "\"?"
            );

            confirmation.setContentText(
                    "Borrower: " +
                            selectedLoanDetails.getBorrowerName()
            );

            confirmation.showAndWait()
                    .ifPresent(result -> {

                        if (result == ButtonType.OK) {

                            try {

                                libraryService.returnBook(
                                        selectedLoan.getLoanId()
                                );

                                loadLoans();

                                showInformation(
                                        "Book Returned",
                                        "The book was returned successfully."
                                );

                            } catch (Exception e) {

                                showError(
                                        "Could not return book.",
                                        e.getMessage()
                                );
                            }
                        }
                    });

        } catch (Exception e) {

            showError(
                    "Could not retrieve loan.",
                    e.getMessage()
            );
        }
    }

    // ============================================================
    // GENERAL DIALOGS
    // ============================================================

    /**
     * Displays an error message.
     */
    private void showError(
            String title,
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alert.setTitle(
                title
        );

        alert.setHeaderText(
                null
        );

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }

    /**
     * Displays an information message.
     */
    private void showInformation(
            String title,
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(
                title
        );

        alert.setHeaderText(
                null
        );

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }

    public static void main(String[] args) {

        launch(args);
    }
}
