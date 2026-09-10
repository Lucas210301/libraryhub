package com.ufape.libraryhub.exception;

public final class MemberAlreadyRegisteredException extends BusinessException {

    public MemberAlreadyRegisteredException(String email) {
        super("The email " + email + " is already registered for another member.");
    }
}
