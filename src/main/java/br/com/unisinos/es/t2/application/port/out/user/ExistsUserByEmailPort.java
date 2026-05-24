package br.com.unisinos.es.t2.application.port.out.user;

public interface ExistsUserByEmailPort {
    boolean existsByEmail(String email);
}
