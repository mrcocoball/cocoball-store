package dev.be.moduleapi.advice.exception;

public class SortTypeInvalidException extends RuntimeException {

    public SortTypeInvalidException() {
        super();
    }

    public SortTypeInvalidException(String message) {
        super(message);
    }

    public SortTypeInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
