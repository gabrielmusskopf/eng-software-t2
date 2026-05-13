package br.com.unisinos.es.t2.application.port.out;

public interface ExistsUserByEmailPort {
    boolean existsByEmail(String email);
}
