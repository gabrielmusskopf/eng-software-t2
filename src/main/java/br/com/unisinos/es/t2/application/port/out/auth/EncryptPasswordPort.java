package br.com.unisinos.es.t2.application.port.out.auth;

public interface EncryptPasswordPort {

    String encrypt(String rawPassword);
}
