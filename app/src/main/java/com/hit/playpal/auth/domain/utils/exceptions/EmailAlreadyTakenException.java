package com.hit.playpal.auth.domain.utils.exceptions;

public class EmailAlreadyTakenException extends RuntimeException {
    public EmailAlreadyTakenException() {
        super("Email is already taken");
    }
}
