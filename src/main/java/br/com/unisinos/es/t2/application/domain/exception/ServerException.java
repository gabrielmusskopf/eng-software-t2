package br.com.unisinos.es.t2.application.domain.exception;

public class ServerException extends RuntimeException {

    public ServerException(String message) {
        super(message);
    }
}
