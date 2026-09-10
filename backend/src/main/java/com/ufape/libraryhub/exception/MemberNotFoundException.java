package com.ufape.libraryhub.exception;

public final class MemberNotFoundException extends BusinessException {

    public MemberNotFoundException(Long id) {
        super("Member " + id + " was not found.");
    }
}
