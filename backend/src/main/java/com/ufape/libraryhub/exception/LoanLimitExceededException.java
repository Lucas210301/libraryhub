package com.ufape.libraryhub.exception;

public final class LoanLimitExceededException extends BusinessException {

    public LoanLimitExceededException(String name, int limit) {
        super(name + " already reached the limit of " + limit + " active loans.");
    }
}
