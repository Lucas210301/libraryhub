package com.ufape.libraryhub.business.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ufape.libraryhub.exception.ItemNotAvailableException;
import org.junit.jupiter.api.Test;

class ItemTest {

    @Test
    void bookIsLentForFifteenDays() {
        Item book = new Book("Clean Code", "Robert Martin", "9780132350884");

        assertEquals(15, book.getLoanDurationInDays());
        assertEquals(ItemType.BOOK, book.getType());
    }

    @Test
    void magazineIsLentForSevenDays() {
        Item magazine = new Magazine("Superinteressante", "Abril", 450);

        assertEquals(7, magazine.getLoanDurationInDays());
        assertEquals(ItemType.MAGAZINE, magazine.getType());
    }

    @Test
    void newItemStartsAvailable() {
        Item book = new Book("Clean Code", "Robert Martin", "9780132350884");

        assertTrue(book.isAvailable());
    }

    @Test
    void lendMakesItemUnavailable() throws ItemNotAvailableException {
        Item book = new Book("Clean Code", "Robert Martin", "9780132350884");

        book.lend();

        assertFalse(book.isAvailable());
    }

    @Test
    void lendTwiceThrowsItemNotAvailableException() throws ItemNotAvailableException {
        Item book = new Book("Clean Code", "Robert Martin", "9780132350884");
        book.lend();

        assertThrows(ItemNotAvailableException.class, book::lend);
    }

    @Test
    void giveBackMakesItemAvailableAgain() throws ItemNotAvailableException {
        Item magazine = new Magazine("Superinteressante", "Abril", 450);
        magazine.lend();

        magazine.giveBack();

        assertTrue(magazine.isAvailable());
    }

    @Test
    void descriptionDependsOnItemSubclass() {
        Item book = new Book("Clean Code", "Robert Martin", "9780132350884");
        Item magazine = new Magazine("Superinteressante", "Abril", 450);

        assertEquals("Robert Martin, ISBN 9780132350884", book.getDescription());
        assertEquals("Abril, edition 450", magazine.getDescription());
    }
}
