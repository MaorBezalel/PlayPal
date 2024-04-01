package com.hit.playpal.auth.domain.utils.exceptions;

public class DisabledAccountException extends RuntimeException {
    public DisabledAccountException() {
        super("Your account has been disabled. Please contact support.");
    }
}