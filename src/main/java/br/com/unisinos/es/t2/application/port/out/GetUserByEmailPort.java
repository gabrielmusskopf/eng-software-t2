package br.com.unisinos.es.t2.application.port.out;

import br.com.unisinos.es.t2.application.domain.model.User;
import java.util.Optional;

public interface GetUserByEmailPort {

    Optional<User> getByEmail(String email);
}
