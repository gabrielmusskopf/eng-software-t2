package br.com.unisinos.es.t2.application.port.in.user;

public interface DeleteUserService {

    void delete(DeleteUserCommand command);

    record DeleteUserCommand(String id) {}
}
