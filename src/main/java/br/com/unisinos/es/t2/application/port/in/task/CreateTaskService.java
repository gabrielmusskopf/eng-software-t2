package br.com.unisinos.es.t2.application.port.in.task;

import br.com.unisinos.es.t2.application.domain.model.Task;

public interface CreateTaskService {

    Task createTask(CreateTaskCommand command);

    record CreateTaskCommand(String title, String description) {}
}
