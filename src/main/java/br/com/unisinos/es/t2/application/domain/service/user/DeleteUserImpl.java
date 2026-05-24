package br.com.unisinos.es.t2.application.domain.service.user;

import br.com.unisinos.es.t2.application.domain.exception.NotFoundException;
import br.com.unisinos.es.t2.application.domain.model.User;
import br.com.unisinos.es.t2.application.port.in.auth.CheckAuthenticatedUserPort;
import br.com.unisinos.es.t2.application.port.in.user.DeleteUserService;
import br.com.unisinos.es.t2.application.port.out.user.DeleteUserPort;
import br.com.unisinos.es.t2.application.port.out.user.GetUserByIdPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class DeleteUserImpl implements DeleteUserService {

    private final CheckAuthenticatedUserPort checkAuthenticatedUserPort;
    private final GetUserByIdPort getUserPort;
    private final DeleteUserPort deleteUserPort;

    @Override
    public void delete(DeleteUserCommand command) {
        User foundUser = getUserPort.getById(command.id()).orElseThrow(() -> new NotFoundException("User not found"));

        if (!checkAuthenticatedUserPort.isAuthenticated(foundUser)) {
            throw new NotFoundException("User not found");
        }

        deleteUserPort.delete(command.id());
    }
}
