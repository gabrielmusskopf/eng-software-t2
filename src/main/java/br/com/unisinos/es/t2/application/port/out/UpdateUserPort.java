package br.com.unisinos.es.t2.application.port.out;

import br.com.unisinos.es.t2.application.domain.model.User;

public interface UpdateUserPort {
    void update(User user);
}
