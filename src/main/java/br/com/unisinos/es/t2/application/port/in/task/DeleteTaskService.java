package br.com.unisinos.es.t2.application.port.in.task;

public interface DeleteTaskService {

    void deleteTask(DeleteTaskCommand command);

    record DeleteTaskCommand(String id) {}
}
