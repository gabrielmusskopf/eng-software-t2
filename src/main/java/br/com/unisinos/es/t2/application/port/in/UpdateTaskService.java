package br.com.unisinos.es.t2.application.port.in;

import br.com.unisinos.es.t2.application.domain.model.TaskStatus;

public interface UpdateTaskService {

    void update(UpdateTaskCommand command);

    record UpdateTaskCommand(String id, String title, String description, TaskStatus status, String assigneeId) {}
}
