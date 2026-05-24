package br.com.unisinos.es.t2.application.port.in;

import br.com.unisinos.es.t2.application.domain.model.Task;

public interface CreateTaskService {

    Task create(CreateTaskCommand command);

    record CreateTaskCommand(String title, String description, String creatorId, String assigneeId) {}
}
