package com.dateplanner.advice.exception;

public class UserNicknameDuplicateException extends RuntimeException {

    public UserNicknameDuplicateException() {
        super();
    }

    public UserNicknameDuplicateException(String message) {
        super(message);
    }

    public UserNicknameDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

}
