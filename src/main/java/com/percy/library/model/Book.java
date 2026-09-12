package com.percy.library.model;

/**
 * Represents a book in the library.
 *
 * This class is part of the model layer and stores
 * information about a book retrieved from the database.
 */
public class Book {

    /*
     * Unique identifier for the book.
     */
    private int bookId;

    /*
     * Title of the book.
     */
    private String title;

    /*
     * Author of the book.
     */
    private String author;

    /*
     * International Standard Book Number.
     */
    private String isbn;

    /*
     * Genre or category of the book.
     */
    private String genre;

    /*
     * Year in which the book was published.
     *
     * Integer is used instead of int because the database
     * allows this value to be NULL.
     */
    private Integer publicationYear;

    /*
     * Indicates whether the book is currently available.
     */
    private boolean available;

    /**
     * Creates an empty Book object.
     *
     * This constructor is useful when creating an object
     * and setting its values later using setter methods.
     */
    public Book() {
    }

    /**
     * Creates a Book object with all its information.
     *
     * @param bookId unique book identifier
     * @param title book title
     * @param author book author
     * @param isbn book ISBN
     * @param genre book genre
     * @param publicationYear year the book was published
     * @param available whether the book is available
     */
    public Book(int bookId, String title, String author, String isbn,
                String genre, Integer publicationYear, boolean available) {

        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.genre = genre;
        this.publicationYear = publicationYear;
        this.available = available;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    /**
     * Returns a readable representation of the book.
     *
     * @return book information as a String
     */
    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", genre='" + genre + '\'' +
                ", publicationYear=" + publicationYear +
                ", available=" + available +
                '}';
    }
}