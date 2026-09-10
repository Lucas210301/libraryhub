package com.ufape.libraryhub.exception;

public final class ItemNotAvailableException extends BusinessException {

    public ItemNotAvailableException(String title) {
        super("The item " + title + " is already on loan.");
    }
}
