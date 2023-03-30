package dev.be.moduleapi.advice.exception;

public class ReviewNotFoundApiException extends RuntimeException {

    public ReviewNotFoundApiException() {
        super();
    }

    public ReviewNotFoundApiException(String message) {
        super(message);
    }

    public ReviewNotFoundApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
