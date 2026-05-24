package br.com.unisinos.es.t2.adapter.out.auth;

import br.com.unisinos.es.t2.application.domain.model.User;
import br.com.unisinos.es.t2.application.port.out.auth.GenerateTokenPort;
import br.com.unisinos.es.t2.config.JwtProperties;
import io.jsonwebtoken.Jwts;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class TokenJwtAdaptor implements GenerateTokenPort {

    private final JwtProperties jwtProperties;

    @Override
    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getId())
                .claim("name", user.getName())
                .claim("email", user.getEmail())
                .issuedAt(new Date())
                .expiration(Date.from(LocalDateTime.now()
                        .plus(jwtProperties.getExpiration())
                        .atZone(ZoneId.systemDefault())
                        .toInstant()))
                .signWith(jwtProperties.secretKey())
                .compact();
    }
}
