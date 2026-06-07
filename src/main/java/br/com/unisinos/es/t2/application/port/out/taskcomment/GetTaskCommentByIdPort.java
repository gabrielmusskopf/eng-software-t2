package br.com.unisinos.es.t2.application.port.out.taskcomment;

import br.com.unisinos.es.t2.application.domain.model.TaskComment;
import java.util.Optional;

public interface GetTaskCommentByIdPort {
    Optional<TaskComment> getById(String id);
}
