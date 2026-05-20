package br.com.unisinos.es.t2.application.port.out.user;

import br.com.unisinos.es.t2.application.domain.model.User;

public interface UpdateUserPort {
    void update(User user);
}
