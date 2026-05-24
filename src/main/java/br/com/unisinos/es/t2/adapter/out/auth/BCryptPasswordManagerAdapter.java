package br.com.unisinos.es.t2.adapter.out.auth;

import br.com.unisinos.es.t2.application.port.out.auth.EncryptPasswordPort;
import br.com.unisinos.es.t2.application.port.out.auth.MatchesPasswordPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class BCryptPasswordManagerAdapter implements EncryptPasswordPort, MatchesPasswordPort {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public String encrypt(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
