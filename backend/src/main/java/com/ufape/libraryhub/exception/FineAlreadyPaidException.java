package com.ufape.libraryhub.exception;

public final class FineAlreadyPaidException extends BusinessException {

    public FineAlreadyPaidException(Long id) {
        super("Fine " + id + " was already paid.");
    }
}
