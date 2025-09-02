package com.discryptment.service.exceptions;

public class RegistrationClosedException extends RuntimeException {
    public RegistrationClosedException(String message) {
        super(message);
    }
}
