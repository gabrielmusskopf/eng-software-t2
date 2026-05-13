package br.com.unisinos.es.t2.application.port.in;

public interface UpdateUserService {

    void update(UpdateUserCommand command);

    record UpdateUserCommand(String id, String name, String email) {}
}
