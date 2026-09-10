package com.ufape.libraryhub.business.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ufape.libraryhub.exception.ReservationNotActiveException;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class ReservationTest {

    private static final LocalDate CREATED_AT = LocalDate.of(2026, 9, 8);

    @Test
    void newReservationStartsActive() {
        Reservation reservation = reservation();

        assertTrue(reservation.isActive());
        assertEquals(ReservationStatus.ACTIVE, reservation.getStatus());
    }

    @Test
    void fulfillClosesTheReservation() throws ReservationNotActiveException {
        Reservation reservation = reservation();

        reservation.fulfill();

        assertEquals(ReservationStatus.FULFILLED, reservation.getStatus());
        assertFalse(reservation.isActive());
    }

    @Test
    void cancelClosesTheReservation() throws ReservationNotActiveException {
        Reservation reservation = reservation();

        reservation.cancel();

        assertEquals(ReservationStatus.CANCELLED, reservation.getStatus());
        assertFalse(reservation.isActive());
    }

    @Test
    void cancelTwiceThrowsReservationNotActiveException() throws ReservationNotActiveException {
        Reservation reservation = reservation();
        reservation.cancel();

        assertThrows(ReservationNotActiveException.class, reservation::cancel);
    }

    @Test
    void fulfillAfterCancelThrowsReservationNotActiveException() throws ReservationNotActiveException {
        Reservation reservation = reservation();
        reservation.cancel();

        assertThrows(ReservationNotActiveException.class, reservation::fulfill);
    }

    @Test
    void belongsToIsFalseWhenTheMemberHasNoIdentifier() {
        assertFalse(reservation().belongsTo(1L));
    }

    private Reservation reservation() {
        Member member = new Member("Ana Souza", "ana.souza@ufape.edu.br");
        Item item = new Book("Clean Code", "Robert Martin", "9780132350884");
        return new Reservation(member, item, CREATED_AT);
    }
}
