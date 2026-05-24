package br.com.unisinos.es.t2.config;

import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.NotBlank;
import java.time.Duration;
import java.util.Objects;
import javax.crypto.SecretKey;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Data
@Component
@Validated
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {
    private String prefix = "Bearer";
    private String header = "Authorization";
    private @NotBlank String privateKey;
    private Duration expiration = Duration.ofHours(1);

    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(Objects.requireNonNull(privateKey).getBytes());
    }
}
