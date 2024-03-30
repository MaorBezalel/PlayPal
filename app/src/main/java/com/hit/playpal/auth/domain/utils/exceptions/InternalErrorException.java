package com.hit.playpal.auth.domain.utils.exceptions;

public class InternalErrorException extends RuntimeException {
    public InternalErrorException() {
        super("Internal error occurred in the server. Please try again later.");
    }
}
