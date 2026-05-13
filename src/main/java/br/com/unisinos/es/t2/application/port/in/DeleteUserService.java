package br.com.unisinos.es.t2.application.port.in;

public interface DeleteUserService {

    void delete(DeleteUserCommand command);

    record DeleteUserCommand(String id) {}
}
