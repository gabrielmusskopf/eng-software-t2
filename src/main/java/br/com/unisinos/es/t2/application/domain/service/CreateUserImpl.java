package br.com.unisinos.es.t2.application.domain.service;

import br.com.unisinos.es.t2.application.domain.exception.ClientException;
import br.com.unisinos.es.t2.application.domain.model.User;
import br.com.unisinos.es.t2.application.port.in.CreateUserService;
import br.com.unisinos.es.t2.application.port.out.ExistsUserByEmailPort;
import br.com.unisinos.es.t2.application.port.out.SaveUserPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class CreateUserImpl implements CreateUserService {

    private final UserMapper userMapper;
    private final ExistsUserByEmailPort existsUserByEmailPort;
    private final SaveUserPort saveUserPort;

    @Override
    public User create(CreateUserCommand command) {
        if (existsUserByEmailPort.existsByEmail(command.email())) {
            log.debug("User tried to create an account with email {}, but it's already taken", command.email());
            throw new ClientException("Email already exists");
        }
        log.debug("Creating user with email {}", command.email());
        User user = userMapper.toUser(command);

        return saveUserPort.save(user);
    }
}
