package org.personal.librarymanagementsystem.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Book extends BaseModel {

    private String name;

    private String author;

    private String publication;

    private String genre;

    private int year;

    private double price;

    public Book(Long id, String name, String author, String publication, String genre, int year, double price) {
        super(id);
        this.name = name;
        this.author = author;
        this.publication = publication;
        this.genre = genre;
        this.year = year;
        this.price = price;
    }

    public Book(String name, String author, String publication, String genre, int year, double price) {
        this.name = name;
        this.author = author;
        this.publication = publication;
        this.genre = genre;
        this.year = year;
        this.price = price;
    }
}
