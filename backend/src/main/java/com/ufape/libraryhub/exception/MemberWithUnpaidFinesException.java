package com.ufape.libraryhub.exception;

public final class MemberWithUnpaidFinesException extends BusinessException {

    public MemberWithUnpaidFinesException(String name) {
        super(name + " has unpaid fines and cannot borrow or be removed.");
    }
}
