package br.com.unisinos.es.t2.application.domain.service.auth;

import br.com.unisinos.es.t2.application.domain.exception.ClientException;
import br.com.unisinos.es.t2.application.domain.model.User;
import br.com.unisinos.es.t2.application.port.in.auth.LoginService;
import br.com.unisinos.es.t2.application.port.out.auth.GenerateTokenPort;
import br.com.unisinos.es.t2.application.port.out.auth.MatchesPasswordPort;
import br.com.unisinos.es.t2.application.port.out.user.GetUserByEmailPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class LoginImpl implements LoginService {

    private final GetUserByEmailPort getUserByEmailPort;
    private final MatchesPasswordPort matchesPasswordPort;
    private final GenerateTokenPort generateTokenPort;

    @Override
    public String generateToken(LoginCommand command) {
        User user = getUserByEmailPort
                .getByEmail(command.email())
                .orElseThrow(() -> new ClientException("Invalid email or password"));

        if (!matchesPasswordPort.matches(command.password(), user.getPassword())) {
            throw new ClientException("Invalid email or password");
        }

        return generateTokenPort.generateToken(user);
    }
}
