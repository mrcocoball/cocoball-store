package com.dateplanner.advice.exception;

public class UserIdDuplicateException extends RuntimeException {

    public UserIdDuplicateException() {
        super();
    }

    public UserIdDuplicateException(String message) {
        super(message);
    }

    public UserIdDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

}
