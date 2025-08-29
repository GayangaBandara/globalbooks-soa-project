package com.globalbooks.catalog;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "GetBookResponse", namespace = "http://catalog.globalbooks.com/")
public class GetBookResponse {
    private String title;
    private String author;
    private double price;

    public GetBookResponse() {}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
