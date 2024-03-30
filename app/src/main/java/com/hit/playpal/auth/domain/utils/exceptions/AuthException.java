package com.hit.playpal.auth.domain.utils.exceptions;

public class AuthException extends Exception {
    private String mErrorCode;
    public String getErrorCode() {
        return mErrorCode;
    }

    public AuthException(String iMessage, String iErrorCode) {
        super(iMessage);
        mErrorCode = iErrorCode;
    }
}