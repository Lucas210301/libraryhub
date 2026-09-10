package com.ufape.libraryhub.business.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("BOOK")
public class Book extends Item {

    private static final int LOAN_DURATION_IN_DAYS = 15;

    private String author;

    private String isbn;

    protected Book() {
        super();
    }

    public Book(String title, String author, String isbn) {
        super(title);
        this.author = author;
        this.isbn = isbn;
    }

    @Override
    public ItemType getType() {
        return ItemType.BOOK;
    }

    @Override
    public int getLoanDurationInDays() {
        return LOAN_DURATION_IN_DAYS;
    }

    @Override
    public String getDescription() {
        return author + ", ISBN " + isbn;
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
}
