package br.com.unisinos.es.t2.application.port.in.auth;

import br.com.unisinos.es.t2.application.domain.model.User;
import java.util.Optional;

public interface GetAuthenticatedUserPort {

    Optional<User> getAuthenticatedUser();
}
