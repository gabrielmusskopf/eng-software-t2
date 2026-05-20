package br.com.unisinos.es.t2.application.port.in.user;

import br.com.unisinos.es.t2.application.domain.model.User;

public interface CreateUserService {

    User create(CreateUserCommand command);

    record CreateUserCommand(String name, String email, String password) {}
}
