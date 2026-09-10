package com.ufape.libraryhub.exception;

public final class ItemNotFoundException extends BusinessException {

    public ItemNotFoundException(Long id) {
        super("Item " + id + " was not found.");
    }
}
