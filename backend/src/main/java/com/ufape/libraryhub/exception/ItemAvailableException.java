package com.ufape.libraryhub.exception;

public final class ItemAvailableException extends BusinessException {

    public ItemAvailableException(String title) {
        super("The item " + title + " is available and can be borrowed instead of reserved.");
    }
}
