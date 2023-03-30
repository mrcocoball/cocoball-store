package dev.be.moduleapi.advice.exception;

public class PlanNotFoundApiException extends RuntimeException {

    public PlanNotFoundApiException() {
        super();
    }

    public PlanNotFoundApiException(String message) {
        super(message);
    }

    public PlanNotFoundApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
