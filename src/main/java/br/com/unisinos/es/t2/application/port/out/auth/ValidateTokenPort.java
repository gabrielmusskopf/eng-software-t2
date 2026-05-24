package br.com.unisinos.es.t2.application.port.out.auth;

public interface ValidateTokenPort {

    boolean isTokenValid(String token);
}
