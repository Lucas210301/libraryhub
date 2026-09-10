package com.ufape.libraryhub.business.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ufape.libraryhub.exception.FineAlreadyPaidException;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class FineTest {

    private static final LocalDate ISSUED_AT = LocalDate.of(2026, 9, 8);

    @Test
    void overdueFineChargesADailyRate() {
        Fine fine = new OverdueFine(loan(), ISSUED_AT, 4);

        assertEquals(new BigDecimal("6.00"), fine.getAmount());
        assertEquals(FineType.OVERDUE, fine.getType());
    }

    @Test
    void damagedItemFineChargesBySeverity() {
        Fine light = new DamagedItemFine(loan(), ISSUED_AT, DamageSeverity.LIGHT);
        Fine severe = new DamagedItemFine(loan(), ISSUED_AT, DamageSeverity.SEVERE);

        assertEquals(new BigDecimal("25.00"), light.getAmount());
        assertEquals(new BigDecimal("60.00"), severe.getAmount());
        assertEquals(FineType.DAMAGE, light.getType());
    }

    @Test
    void reasonDependsOnFineSubclass() {
        Fine overdue = new OverdueFine(loan(), ISSUED_AT, 4);
        Fine damage = new DamagedItemFine(loan(), ISSUED_AT, DamageSeverity.SEVERE);

        assertEquals("Returned 4 days after the due date", overdue.getReason());
        assertEquals("Item returned with severe damage", damage.getReason());
    }

    @Test
    void newFineStartsUnpaid() {
        Fine fine = new OverdueFine(loan(), ISSUED_AT, 1);

        assertFalse(fine.isPaid());
    }

    @Test
    void payRegistersThePaymentDate() throws FineAlreadyPaidException {
        Fine fine = new OverdueFine(loan(), ISSUED_AT, 1);

        fine.pay(LocalDate.of(2026, 9, 10));

        assertTrue(fine.isPaid());
        assertEquals(LocalDate.of(2026, 9, 10), fine.getPaymentDate());
    }

    @Test
    void payTwiceThrowsFineAlreadyPaidException() throws FineAlreadyPaidException {
        Fine fine = new OverdueFine(loan(), ISSUED_AT, 1);
        fine.pay(LocalDate.of(2026, 9, 10));

        assertThrows(FineAlreadyPaidException.class, () -> fine.pay(LocalDate.of(2026, 9, 11)));
    }

    private Loan loan() {
        Member member = new Member("Ana Souza", "ana.souza@ufape.edu.br");
        Item item = new Book("Clean Code", "Robert Martin", "9780132350884");
        return new Loan(member, item, LocalDate.of(2026, 8, 20));
    }
}
