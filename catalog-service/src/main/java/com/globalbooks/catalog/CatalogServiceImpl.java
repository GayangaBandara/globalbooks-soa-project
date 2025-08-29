package com.globalbooks.catalog;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.xml.bind.annotation.XmlElement;

@WebService(
    serviceName = "CatalogService",
    portName = "CatalogPort",
    endpointInterface = "com.globalbooks.catalog.CatalogPortType",
    targetNamespace = "http://catalog.globalbooks.com/"
)
public class CatalogServiceImpl {

    @WebMethod(operationName = "getBook")
    public GetBookResponse getBook(
        @WebParam(name = "parameters", targetNamespace = "http://catalog.globalbooks.com/")
        GetBookRequest request) {

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

    // Inner class for the Request. @XmlElement links it to the XSD element.
    public static class GetBookRequest {
        private String isbn;

        @XmlElement(name = "isbn", namespace = "http://catalog.globalbooks.com/")
        public String getIsbn() { return isbn; }
        public void setIsbn(String isbn) { this.isbn = isbn; }
    }

    // Inner class for the Response
    @javax.xml.bind.annotation.XmlType(name = "BookResponse", namespace = "http://catalog.globalbooks.com/")
    public static class GetBookResponse {
        private String title;
        private String author;
        private double price;

        @XmlElement(name = "title", namespace = "http://catalog.globalbooks.com/")
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        @XmlElement(name = "author", namespace = "http://catalog.globalbooks.com/")
        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }

        @XmlElement(name = "price", namespace = "http://catalog.globalbooks.com/")
        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }
    }
}