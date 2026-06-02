package br.com.unisinos.es.t2.application.port.in.task;

import br.com.unisinos.es.t2.application.domain.model.Task;
import br.com.unisinos.es.t2.application.domain.model.TaskStatus;

public interface UpdateTaskService {

    Task updateTask(UpdateTaskCommand command);

    record UpdateTaskCommand(String taskId, String title, String description, TaskStatus status, String assigneeId) {}
}
