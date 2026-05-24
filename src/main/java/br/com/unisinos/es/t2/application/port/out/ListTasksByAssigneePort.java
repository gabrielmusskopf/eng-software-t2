package br.com.unisinos.es.t2.application.port.out;

import br.com.unisinos.es.t2.application.domain.model.Task;
import java.util.List;

public interface ListTasksByAssigneePort {

    List<Task> listByAssigneeId(String assigneeId);
}
