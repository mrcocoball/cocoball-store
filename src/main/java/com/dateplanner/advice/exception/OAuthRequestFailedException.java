package com.dateplanner.advice.exception;

public class OAuthRequestFailedException extends RuntimeException {

    public OAuthRequestFailedException() {
        super();
    }

    public OAuthRequestFailedException(String message) {
        super(message);
    }

    public OAuthRequestFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
