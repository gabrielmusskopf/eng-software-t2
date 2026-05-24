package br.com.unisinos.es.t2.application.domain.service.user;

import br.com.unisinos.es.t2.application.domain.exception.NotAuthenticatedException;
import br.com.unisinos.es.t2.application.domain.model.User;
import br.com.unisinos.es.t2.application.port.in.auth.GetAuthenticatedUserPort;
import br.com.unisinos.es.t2.application.port.in.user.GetAuthenticatedUserService;
import br.com.unisinos.es.t2.application.port.out.user.GetUserByIdPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class GetAuthenticatedUserImpl implements GetAuthenticatedUserService {

    private final GetAuthenticatedUserPort getAuthenticatedUserPort;
    private final GetUserByIdPort getUserPort;

    @Override
    public User get() {
        return getAuthenticatedUserPort
                .getAuthenticatedUser()
                .orElseThrow(() -> new NotAuthenticatedException("Authenticated user not found"));
    }
}
