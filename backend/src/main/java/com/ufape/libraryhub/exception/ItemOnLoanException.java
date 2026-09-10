package com.ufape.libraryhub.exception;

public final class ItemOnLoanException extends BusinessException {

    public ItemOnLoanException(String title) {
        super("The item " + title + " is on loan and cannot be removed.");
    }
}
