package br.com.unisinos.es.t2.application.domain.service.user;

import br.com.unisinos.es.t2.application.domain.exception.NotFoundException;
import br.com.unisinos.es.t2.application.domain.model.User;
import br.com.unisinos.es.t2.application.port.in.user.GetUserService;
import br.com.unisinos.es.t2.application.port.out.user.GetUserByIdPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class GetUserImpl implements GetUserService {

    private final GetUserByIdPort getUserPort;

    @Override
    public User get(GetUserCommand command) {
        return getUserPort.getById(command.id()).orElseThrow(() -> new NotFoundException("User not found"));
    }
}
