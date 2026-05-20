package br.com.unisinos.es.t2.application.domain.service.user;

import br.com.unisinos.es.t2.application.domain.exception.NotFoundException;
import br.com.unisinos.es.t2.application.port.in.user.DeleteUserService;
import br.com.unisinos.es.t2.application.port.out.user.DeleteUserPort;
import br.com.unisinos.es.t2.application.port.out.user.ExistsUserByIdPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class DeleteUserImpl implements DeleteUserService {

    private final ExistsUserByIdPort existsUserByIdPort;
    private final DeleteUserPort deleteUserPort;

    @Override
    public void delete(DeleteUserCommand command) {
        if (!existsUserByIdPort.exists(command.id())) {
            throw new NotFoundException("User not found");
        }
        deleteUserPort.delete(command.id());
    }
}
