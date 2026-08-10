package com.finpilot.exception;

public class PasswordResetTokenAlreadyUsedException extends RuntimeException {

    public PasswordResetTokenAlreadyUsedException(String message) {
        super(message);
    }
}