package br.com.unisinos.es.t2.application.port.in.auth;

public interface LoginService {

    String generateToken(LoginCommand command);

    record LoginCommand(String email, String password) {}
}
