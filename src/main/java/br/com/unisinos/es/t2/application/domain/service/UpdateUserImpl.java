package br.com.unisinos.es.t2.application.domain.service;

import br.com.unisinos.es.t2.application.domain.exception.ClientException;
import br.com.unisinos.es.t2.application.domain.exception.NotFoundException;
import br.com.unisinos.es.t2.application.domain.model.User;
import br.com.unisinos.es.t2.application.port.in.UpdateUserService;
import br.com.unisinos.es.t2.application.port.out.GetUserByEmailPort;
import br.com.unisinos.es.t2.application.port.out.GetUserByIdPort;
import br.com.unisinos.es.t2.application.port.out.UpdateUserPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class UpdateUserImpl implements UpdateUserService {

    private final GetUserByIdPort getUserPort;
    private final GetUserByEmailPort getUserByEmailPort;
    private final UpdateUserPort updateUserPort;

    @Override
    public void update(UpdateUserCommand command) {
        boolean anotherUserHasEmail = getUserByEmailPort
                .getByEmail(command.email())
                .filter(user -> !user.getId().equals(command.id()))
                .isPresent();

        if (anotherUserHasEmail) {
            log.debug("User {} tried to update email to {}, but it's already taken", command.id(), command.email());
            throw new ClientException("Email is taken");
        }

        User foundUser = getUserPort.getById(command.id()).orElseThrow(() -> new NotFoundException("User not found"));

        if (command.name() != null) {
            log.debug("Updating user {} name from {} to {}", command.id(), foundUser.getName(), command.name());
            foundUser.setName(command.name());
        }
        if (command.email() != null) {
            log.debug("Updating user {} name from {} to {}", command.id(), foundUser.getName(), command.name());
            foundUser.setEmail(command.email());
        }
        updateUserPort.update(foundUser);
    }
}
