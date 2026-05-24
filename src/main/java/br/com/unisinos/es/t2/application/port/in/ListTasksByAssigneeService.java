package br.com.unisinos.es.t2.application.port.in;

import br.com.unisinos.es.t2.application.domain.model.Task;
import java.util.List;

public interface ListTasksByAssigneeService {

    List<Task> list(ListTasksByAssigneeCommand command);

    record ListTasksByAssigneeCommand(String assigneeId) {}
}
