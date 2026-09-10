package com.ufape.libraryhub.exception;

public abstract class BusinessException extends Exception {

    protected BusinessException(String message) {
        super(message);
    }
}
