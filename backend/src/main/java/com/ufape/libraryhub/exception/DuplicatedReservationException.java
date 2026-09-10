package com.ufape.libraryhub.exception;

public final class DuplicatedReservationException extends BusinessException {

    public DuplicatedReservationException(String memberName, String itemTitle) {
        super(memberName + " already has an active reservation for " + itemTitle + ".");
    }
}
