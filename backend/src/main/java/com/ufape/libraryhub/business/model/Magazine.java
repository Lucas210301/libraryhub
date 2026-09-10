package com.ufape.libraryhub.business.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("MAGAZINE")
public class Magazine extends Item {

    private static final int LOAN_DURATION_IN_DAYS = 7;

    private String publisher;

    private Integer edition;

    protected Magazine() {
        super();
    }

    public Magazine(String title, String publisher, Integer edition) {
        super(title);
        this.publisher = publisher;
        this.edition = edition;
    }

    @Override
    public ItemType getType() {
        return ItemType.MAGAZINE;
    }

    @Override
    public int getLoanDurationInDays() {
        return LOAN_DURATION_IN_DAYS;
    }

    @Override
    public String getDescription() {
        return publisher + ", edition " + edition;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Integer getEdition() {
        return edition;
    }

    public void setEdition(Integer edition) {
        this.edition = edition;
    }
}
