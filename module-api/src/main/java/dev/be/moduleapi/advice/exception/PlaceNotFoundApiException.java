package dev.be.moduleapi.advice.exception;

public class PlaceNotFoundApiException extends RuntimeException {

    public PlaceNotFoundApiException() {
        super();
    }

    public PlaceNotFoundApiException(String message) {
        super(message);
    }

    public PlaceNotFoundApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
