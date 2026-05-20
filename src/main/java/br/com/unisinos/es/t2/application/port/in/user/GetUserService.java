package br.com.unisinos.es.t2.application.port.in.user;

import br.com.unisinos.es.t2.application.domain.model.User;

public interface GetUserService {

    User get(GetUserCommand command);

    record GetUserCommand(String id) {}
}
