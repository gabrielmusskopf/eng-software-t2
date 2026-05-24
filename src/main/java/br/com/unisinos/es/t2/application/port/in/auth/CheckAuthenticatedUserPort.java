package br.com.unisinos.es.t2.application.port.in.auth;

import br.com.unisinos.es.t2.application.domain.model.User;

public interface CheckAuthenticatedUserPort {

    boolean isAuthenticated(User user);
}
