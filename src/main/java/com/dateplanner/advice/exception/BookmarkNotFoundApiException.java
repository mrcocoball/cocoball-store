package com.dateplanner.advice.exception;

public class BookmarkNotFoundApiException extends RuntimeException {

    public BookmarkNotFoundApiException() {
        super();
    }

    public BookmarkNotFoundApiException(String message) {
        super(message);
    }

    public BookmarkNotFoundApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
