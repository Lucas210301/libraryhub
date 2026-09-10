package com.ufape.libraryhub.business.model;

import com.ufape.libraryhub.exception.ItemNotAvailableException;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity
@Table(name = "item")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "item_type")
public abstract class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private boolean available = true;

    protected Item() {
    }

    protected Item(String title) {
        this.title = title;
        this.available = true;
    }

    public abstract ItemType getType();

    public abstract int getLoanDurationInDays();

    public abstract String getDescription();

    public void lend() throws ItemNotAvailableException {
        if (!available) {
            throw new ItemNotAvailableException(title);
        }
        available = false;
    }

    public void giveBack() {
        available = true;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isAvailable() {
        return available;
    }
}
