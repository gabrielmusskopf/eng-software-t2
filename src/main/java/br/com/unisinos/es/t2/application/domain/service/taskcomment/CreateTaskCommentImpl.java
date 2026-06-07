package br.com.unisinos.es.t2.application.domain.service.taskcomment;

import br.com.unisinos.es.t2.application.domain.exception.NotAuthenticatedException;
import br.com.unisinos.es.t2.application.domain.exception.NotFoundException;
import br.com.unisinos.es.t2.application.domain.model.TaskComment;
import br.com.unisinos.es.t2.application.domain.model.User;
import br.com.unisinos.es.t2.application.port.in.auth.GetAuthenticatedUserPort;
import br.com.unisinos.es.t2.application.port.in.taskcomment.CreateTaskCommentService;
import br.com.unisinos.es.t2.application.port.out.task.GetTaskByIdPort;
import br.com.unisinos.es.t2.application.port.out.taskcomment.CreateTaskCommentPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class CreateTaskCommentImpl implements CreateTaskCommentService {

    private final GetAuthenticatedUserPort getAuthenticatedUserPort;
    private final GetTaskByIdPort getTaskByIdPort;
    private final CreateTaskCommentPort createTaskCommentPort;

    @Override
    public TaskComment createComment(CreateTaskCommentCommand command) {
        log.debug("Creating comment on task id {}", command.taskId());
        User user = getAuthenticatedUserPort.getAuthenticatedUser().orElseThrow(NotAuthenticatedException::new);
        getTaskByIdPort.getById(command.taskId()).orElseThrow(() -> new NotFoundException("Task not found"));

        TaskComment comment = new TaskComment(command.taskId(), user.getId(), command.content());
        return createTaskCommentPort.create(comment);
    }
}
