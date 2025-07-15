package com.petreca.exceptions;

public class AccountInUseException extends RuntimeException {
    public AccountInUseException(String message) {
        super(message);
    }
}
