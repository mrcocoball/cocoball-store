package com.dateplanner.advice.exception;

public class SearchResultNotFoundException extends RuntimeException {

    public SearchResultNotFoundException() {
        super();
    }

    public SearchResultNotFoundException(String message) {
        super(message);
    }

    public SearchResultNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
