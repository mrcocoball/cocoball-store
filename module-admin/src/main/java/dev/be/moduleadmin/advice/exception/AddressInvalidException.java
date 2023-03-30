package dev.be.moduleadmin.advice.exception;

public class AddressInvalidException extends RuntimeException {

    public AddressInvalidException() {
        super();
    }

    public AddressInvalidException(String message) {
        super(message);
    }

    public AddressInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
