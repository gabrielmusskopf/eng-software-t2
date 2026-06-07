package br.com.unisinos.es.t2.application.port.in.taskcomment;

import br.com.unisinos.es.t2.application.domain.model.TaskComment;
import java.util.List;

public interface GetTaskCommentsService {

    List<TaskComment> getComments(GetTaskCommentsCommand command);

    record GetTaskCommentsCommand(String taskId) {}
}
