package com.hit.playpal.auth.domain.utils.exceptions;

public class InvalidDetailsException extends RuntimeException {
    public InvalidDetailsException() {
        super("The login details are incorrect.");
    }
}