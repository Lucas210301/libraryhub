package com.ufape.libraryhub.exception;

public final class FineNotFoundException extends BusinessException {

    public FineNotFoundException(Long id) {
        super("Fine " + id + " was not found.");
    }
}
