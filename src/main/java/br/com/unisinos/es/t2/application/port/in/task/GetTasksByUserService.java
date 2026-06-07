package br.com.unisinos.es.t2.application.port.in.task;

import br.com.unisinos.es.t2.application.domain.model.Task;
import java.util.List;

public interface GetTasksByUserService {

    List<Task> getByAssignedUser(GetTasksByUserCommand command);

    record GetTasksByUserCommand(String userId) {}
}
