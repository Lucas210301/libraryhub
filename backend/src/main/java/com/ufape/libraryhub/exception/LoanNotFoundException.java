package com.ufape.libraryhub.exception;

public final class LoanNotFoundException extends BusinessException {

    public LoanNotFoundException(Long id) {
        super("Loan " + id + " was not found.");
    }
}
