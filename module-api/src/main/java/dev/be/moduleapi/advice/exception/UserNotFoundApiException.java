package dev.be.moduleapi.advice.exception;

public class UserNotFoundApiException extends RuntimeException {

    public UserNotFoundApiException() {
        super();
    }

    public UserNotFoundApiException(String message) {
        super(message);
    }

    public UserNotFoundApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
