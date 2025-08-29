package com.globalbooks.catalog;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "GetBookRequest", namespace = "http://catalog.globalbooks.com/")
public class GetBookRequest {
    private String isbn;

    public GetBookRequest() {}

    public GetBookRequest(String isbn) {
        this.isbn = isbn;
    }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
}
