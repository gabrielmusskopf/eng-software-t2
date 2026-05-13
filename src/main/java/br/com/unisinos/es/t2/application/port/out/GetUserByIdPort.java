package br.com.unisinos.es.t2.application.port.out;

import br.com.unisinos.es.t2.application.domain.model.User;
import java.util.Optional;

public interface GetUserByIdPort {

    Optional<User> getById(String id);
}
