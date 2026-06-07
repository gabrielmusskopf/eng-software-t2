package br.com.unisinos.es.t2.application.port.out.task;

import br.com.unisinos.es.t2.application.domain.model.Task;
import java.util.List;

public interface GetTasksByUserIdPort {
    List<Task> getByUserId(String userId);
}
