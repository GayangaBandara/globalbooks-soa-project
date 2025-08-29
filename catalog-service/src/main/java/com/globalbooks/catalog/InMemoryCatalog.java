package com.globalbooks.catalog;

import java.util.HashMap;
import java.util.Map;

public class InMemoryCatalog {
    private static final Map<String, Book> bookDatabase = new HashMap<>();
    
    static {
        bookDatabase.put("978-0134685991", new Book("978-0134685991", "Effective Java", "Joshua Bloch", 54.99));
        bookDatabase.put("978-1617294945", new Book("978-1617294945", "Spring in Action", "Craig Walls", 39.99));
        bookDatabase.put("978-1491950357", new Book("978-1491950357", "Building Microservices", "Sam Newman", 49.99));
    }
    
    public static Book findByIsbn(String isbn) {
        return bookDatabase.get(isbn);
    }
}