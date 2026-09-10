package com.ufape.libraryhub.exception;

public final class DuplicatedFineException extends BusinessException {

    public DuplicatedFineException(Long loanId) {
        super("Loan " + loanId + " already has a damage fine.");
    }
}
