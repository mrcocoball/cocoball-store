package com.dateplanner.advice.exception;

public class BookmarkDuplicateException extends RuntimeException {

    public BookmarkDuplicateException() {
        super();
    }

    public BookmarkDuplicateException(String message) {
        super(message);
    }

    public BookmarkDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }
}
