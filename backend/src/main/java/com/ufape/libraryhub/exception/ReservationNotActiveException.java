package com.ufape.libraryhub.exception;

import com.ufape.libraryhub.business.model.ReservationStatus;

public final class ReservationNotActiveException extends BusinessException {

    public ReservationNotActiveException(Long id, ReservationStatus status) {
        super("Reservation " + id + " is " + status.name().toLowerCase() + " and cannot change anymore.");
    }
}
