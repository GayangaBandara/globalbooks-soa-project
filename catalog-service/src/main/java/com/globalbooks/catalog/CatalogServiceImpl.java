package com.globalbooks.catalog;

import javax.jws.WebService;

@WebService(
    endpointInterface = "com.globalbooks.catalog.CatalogPortType",
    serviceName = "CatalogService",
    portName = "CatalogPort",
    targetNamespace = "http://catalog.globalbooks.com/"
)
public class CatalogServiceImpl {
    
    public GetBookResponse getBook(GetBookRequest request) {
        String isbn = request.getIsbn();
        Book book = InMemoryCatalog.findByIsbn(isbn);
        
        GetBookResponse response = new GetBookResponse();
        if (book != null) {
            response.setTitle(book.getTitle());
            response.setAuthor(book.getAuthor());
            response.setPrice(book.getPrice());
        }
        return response;
    }
    
    // Inner classes for request/response
    public static class GetBookRequest {
        private String isbn;
        // getter and setter
        public String getIsbn() { return isbn; }
        public void setIsbn(String isbn) { this.isbn = isbn; }
    }
    
    public static class GetBookResponse {
        private String title;
        private String author;
        private double price;
        // getters and setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }
        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }
    }
}