package com.globalbooks.catalog;

public class Book {
    private String isbn;
    private String title;
    private String author;
    private double price;

    // Constructor, Getters and Setters
    public Book(String isbn, String title, String author, double price) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.price = price;
    }
    // ... getters and setters ...
    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public double getPrice() {
        return price;
    }

}