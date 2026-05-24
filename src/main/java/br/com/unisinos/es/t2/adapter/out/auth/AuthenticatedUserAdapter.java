package br.com.unisinos.es.t2.adapter.out.auth;

import br.com.unisinos.es.t2.application.domain.model.User;
import br.com.unisinos.es.t2.application.port.in.auth.CheckAuthenticatedUserPort;
import br.com.unisinos.es.t2.application.port.in.auth.GetAuthenticatedUserPort;
import br.com.unisinos.es.t2.application.port.out.user.GetUserByIdPort;
import br.com.unisinos.es.t2.config.AuthenticatedUser;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class AuthenticatedUserAdapter implements GetAuthenticatedUserPort, CheckAuthenticatedUserPort {

    private final GetUserByIdPort getUserByIdPort;

    @Override
    public Optional<User> getAuthenticatedUser() {
        return this.getAuthenticated().map(AuthenticatedUser::getId).flatMap(getUserByIdPort::getById);
    }

    @Override
    public boolean isAuthenticated(User user) {
        return this.getAuthenticated()
                .map(authenticatedUser -> authenticatedUser.getId().equals(user.getId()))
                .orElse(false);
    }

    private Optional<AuthenticatedUser> getAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            log.debug("No authenticated user found in security context");
            return Optional.empty();
        }
        log.debug("Authenticated user found: {}", authentication.getName());
        if (authentication.getPrincipal() instanceof AuthenticatedUser authenticatedUser) {
            return Optional.of(authenticatedUser);
        }
        return Optional.empty();
    }
}
