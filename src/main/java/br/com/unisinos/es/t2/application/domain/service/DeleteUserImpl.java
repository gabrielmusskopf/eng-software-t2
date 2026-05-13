package br.com.unisinos.es.t2.application.domain.service;

import br.com.unisinos.es.t2.application.domain.exception.NotFoundException;
import br.com.unisinos.es.t2.application.port.in.DeleteUserService;
import br.com.unisinos.es.t2.application.port.out.DeleteUserPort;
import br.com.unisinos.es.t2.application.port.out.ExistsUserByEmailPort;
import br.com.unisinos.es.t2.application.port.out.ExistsUserByIdPort;

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
