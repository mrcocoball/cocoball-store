package com.dateplanner.advice.exception;

public class CategoryInvalidException extends RuntimeException {

    public CategoryInvalidException() {
        super();
    }

    public CategoryInvalidException(String message) {
        super(message);
    }

    public CategoryInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
