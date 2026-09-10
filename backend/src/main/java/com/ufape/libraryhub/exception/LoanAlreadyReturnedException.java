package com.ufape.libraryhub.exception;

public final class LoanAlreadyReturnedException extends BusinessException {

    public LoanAlreadyReturnedException(Long id) {
        super("Loan " + id + " was already returned.");
    }
}
