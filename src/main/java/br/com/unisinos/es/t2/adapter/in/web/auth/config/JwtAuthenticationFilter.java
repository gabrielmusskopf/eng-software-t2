package br.com.unisinos.es.t2.adapter.in.web.auth.config;

import br.com.unisinos.es.t2.config.AuthenticatedUser;
import br.com.unisinos.es.t2.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String PREFIX = "Bearer ";
    private static final String AUTHORIZATION = "Authorization";

    private final JwtProperties jwtProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader(jwtProperties.getHeader());
        if (authHeader == null || !authHeader.startsWith(jwtProperties.getPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(jwtProperties.getPrefix().length()).strip();
        try {
            Claims claims = this.getTokenClaims(jwt);
            String id = this.extractUserId(claims);
            String username = this.extractUsername(claims);
            String email = this.extractEmail(claims);

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                AuthenticatedUser authenticatedUser = new AuthenticatedUser(id, username, email);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        authenticatedUser, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            log.debug("Invalid JWT token: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private Claims getTokenClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtProperties.secretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private String extractUserId(Claims claims) {
        return claims.getSubject();
    }

    private String extractUsername(Claims claims) {
        return claims.get("name", String.class);
    }

    private String extractEmail(Claims claims) {
        return claims.get("email", String.class);
    }
}
