package com.dateplanner.advice.exception;

public class DetailPlanNotFoundApiException extends RuntimeException {

    public DetailPlanNotFoundApiException() {
        super();
    }

    public DetailPlanNotFoundApiException(String message) {
        super(message);
    }

    public DetailPlanNotFoundApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
