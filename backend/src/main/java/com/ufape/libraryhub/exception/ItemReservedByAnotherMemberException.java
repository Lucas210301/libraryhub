package com.ufape.libraryhub.exception;

public final class ItemReservedByAnotherMemberException extends BusinessException {

    public ItemReservedByAnotherMemberException(String title) {
        super("The item " + title + " is reserved for another member in the queue.");
    }
}
