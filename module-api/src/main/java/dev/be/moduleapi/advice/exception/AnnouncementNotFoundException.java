package dev.be.moduleapi.advice.exception;

public class AnnouncementNotFoundException extends RuntimeException {

    public AnnouncementNotFoundException() {
        super();
    }

    public AnnouncementNotFoundException(String message) {
        super(message);
    }

    public AnnouncementNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
