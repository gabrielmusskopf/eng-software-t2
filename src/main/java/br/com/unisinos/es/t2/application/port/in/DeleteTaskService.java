package br.com.unisinos.es.t2.application.port.in;

public interface DeleteTaskService {

    void delete(DeleteTaskCommand command);

    record DeleteTaskCommand(String id) {}
}
