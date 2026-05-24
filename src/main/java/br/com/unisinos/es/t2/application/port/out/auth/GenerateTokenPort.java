package br.com.unisinos.es.t2.application.port.out.auth;

import br.com.unisinos.es.t2.application.domain.model.User;

public interface GenerateTokenPort {

    String generateToken(User user);
}
