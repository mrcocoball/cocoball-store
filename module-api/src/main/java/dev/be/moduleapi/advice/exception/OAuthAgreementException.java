package dev.be.moduleapi.advice.exception;

public class OAuthAgreementException extends RuntimeException {

    public OAuthAgreementException() {
        super();
    }

    public OAuthAgreementException(String message) {
        super(message);
    }

    public OAuthAgreementException(String message, Throwable cause) {
        super(message, cause);
    }
}
