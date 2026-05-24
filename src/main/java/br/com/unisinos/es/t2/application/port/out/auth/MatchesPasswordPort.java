package br.com.unisinos.es.t2.application.port.out.auth;

public interface MatchesPasswordPort {

    boolean matches(String rawPassword, String encodedPassword);
}
