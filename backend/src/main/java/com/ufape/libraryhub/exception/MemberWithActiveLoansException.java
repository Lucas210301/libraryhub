package com.ufape.libraryhub.exception;

public final class MemberWithActiveLoansException extends BusinessException {

    public MemberWithActiveLoansException(String name) {
        super(name + " still has active loans and cannot be removed.");
    }
}
