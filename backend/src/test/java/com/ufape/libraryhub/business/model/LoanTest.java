package com.ufape.libraryhub.business.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ufape.libraryhub.exception.LoanAlreadyReturnedException;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class LoanTest {

    private static final LocalDate LOAN_DATE = LocalDate.of(2026, 3, 10);

    @Test
    void dueDateOfBookLoanIsFifteenDaysAhead() {
        Loan loan = new Loan(member(), new Book("Clean Code", "Robert Martin", "9780132350884"), LOAN_DATE);

        assertEquals(LocalDate.of(2026, 3, 25), loan.getDueDate());
    }

    @Test
    void dueDateOfMagazineLoanIsSevenDaysAhead() {
        Loan loan = new Loan(member(), new Magazine("Superinteressante", "Abril", 450), LOAN_DATE);

        assertEquals(LocalDate.of(2026, 3, 17), loan.getDueDate());
    }

    @Test
    void newLoanIsActive() {
        Loan loan = new Loan(member(), new Book("Clean Code", "Robert Martin", "9780132350884"), LOAN_DATE);

        assertTrue(loan.isActive());
    }

    @Test
    void finishRegistersReturnDateAndReleasesItem() throws Exception {
        Item book = new Book("Clean Code", "Robert Martin", "9780132350884");
        book.lend();
        Loan loan = new Loan(member(), book, LOAN_DATE);

        loan.finish(LocalDate.of(2026, 3, 20));

        assertEquals(LocalDate.of(2026, 3, 20), loan.getReturnDate());
        assertFalse(loan.isActive());
        assertTrue(book.isAvailable());
    }

    @Test
    void finishTwiceThrowsLoanAlreadyReturnedException() throws Exception {
        Loan loan = new Loan(member(), new Book("Clean Code", "Robert Martin", "9780132350884"), LOAN_DATE);
        loan.finish(LocalDate.of(2026, 3, 20));

        assertThrows(LoanAlreadyReturnedException.class, () -> loan.finish(LocalDate.of(2026, 3, 21)));
    }

    @Test
    void loanIsOverdueAfterDueDate() {
        Loan loan = new Loan(member(), new Magazine("Superinteressante", "Abril", 450), LOAN_DATE);

        assertTrue(loan.isOverdue(LocalDate.of(2026, 3, 18)));
        assertFalse(loan.isOverdue(LocalDate.of(2026, 3, 15)));
    }

    private Member member() {
        return new Member("Ana Souza", "ana.souza@ufape.edu.br");
    }
}
