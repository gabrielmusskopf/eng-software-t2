package br.com.unisinos.es.t2.application.port.out.taskcomment;

import br.com.unisinos.es.t2.application.domain.model.TaskComment;

public interface CreateTaskCommentPort {
    TaskComment create(TaskComment comment);
}
