package br.com.unisinos.es.t2.application.domain.exception;

public class NotAuthenticatedException extends RuntimeException {

    public NotAuthenticatedException() {
        super("Not authenticated");
    }

    public NotAuthenticatedException(String message) {
        super(message);
    }
}
