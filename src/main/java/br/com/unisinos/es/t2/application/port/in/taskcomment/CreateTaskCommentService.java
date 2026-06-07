package br.com.unisinos.es.t2.application.port.in.taskcomment;

import br.com.unisinos.es.t2.application.domain.model.TaskComment;

public interface CreateTaskCommentService {

    TaskComment createComment(CreateTaskCommentCommand command);

    record CreateTaskCommentCommand(String taskId, String content) {}
}
