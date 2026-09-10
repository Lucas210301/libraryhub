package com.ufape.libraryhub.exception;

public final class ReservationNotFoundException extends BusinessException {

    public ReservationNotFoundException(Long id) {
        super("Reservation " + id + " was not found.");
    }
}
