package br.com.unisinos.es.t2.application.domain.service.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import br.com.unisinos.es.t2.application.domain.exception.ClientException;
import br.com.unisinos.es.t2.application.domain.model.User;
import br.com.unisinos.es.t2.application.port.in.auth.LoginService;
import br.com.unisinos.es.t2.application.port.out.auth.GenerateTokenPort;
import br.com.unisinos.es.t2.application.port.out.auth.MatchesPasswordPort;
import br.com.unisinos.es.t2.application.port.out.user.GetUserByEmailPort;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoginImplTest {

    @InjectMocks
    private LoginImpl loginImpl;

    @Mock
    private GetUserByEmailPort getUserByEmailPort;

    @Mock
    private MatchesPasswordPort matchesPasswordPort;

    @Mock
    private GenerateTokenPort generateTokenPort;

    private final EasyRandom easyRandom = new EasyRandom();

    @Test
    void generateTokenShouldErrorWhenEmailNotFound() {

        when(getUserByEmailPort.getByEmail(anyString())).thenReturn(java.util.Optional.empty());

        LoginService.LoginCommand command = new LoginService.LoginCommand("email", "password");
        ClientException exception = assertThrows(ClientException.class, () -> loginImpl.generateToken(command));

        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    void generateTokenShouldErrorWhenPasswordDoesNotMatch() {
        var user = easyRandom.nextObject(User.class);

        when(getUserByEmailPort.getByEmail(anyString())).thenReturn(java.util.Optional.of(user));
        when(matchesPasswordPort.matches(anyString(), anyString())).thenReturn(false);

        LoginService.LoginCommand command = new LoginService.LoginCommand("email", "password");
        ClientException exception = assertThrows(ClientException.class, () -> loginImpl.generateToken(command));

        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    void generateTokenShouldReturnTokenWhenSuccess() {
        var user = easyRandom.nextObject(User.class);
        String expectedToken = "valid.jwt.token";

        when(getUserByEmailPort.getByEmail(anyString())).thenReturn(java.util.Optional.of(user));
        when(matchesPasswordPort.matches(anyString(), anyString())).thenReturn(true);
        when(generateTokenPort.generateToken(user)).thenReturn(expectedToken);

        LoginService.LoginCommand command = new LoginService.LoginCommand("email", "password");
        String actualToken = loginImpl.generateToken(command);

        assertEquals(expectedToken, actualToken);
    }
}
