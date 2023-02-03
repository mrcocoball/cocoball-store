package com.dateplanner.advice.exception;

public class CustomRetryException extends RuntimeException {

    public CustomRetryException() {
        super();
    }

    public CustomRetryException(String message) {
        super(message);
    }

    public CustomRetryException(String message, Throwable cause) {
        super(message, cause);
    }
}
