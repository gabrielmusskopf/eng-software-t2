package br.com.unisinos.es.t2.application.domain.service.taskcomment;

import br.com.unisinos.es.t2.application.domain.exception.NotAuthenticatedException;
import br.com.unisinos.es.t2.application.domain.exception.NotFoundException;
import br.com.unisinos.es.t2.application.domain.model.TaskComment;
import br.com.unisinos.es.t2.application.domain.model.User;
import br.com.unisinos.es.t2.application.port.in.auth.GetAuthenticatedUserPort;
import br.com.unisinos.es.t2.application.port.in.taskcomment.DeleteTaskCommentService;
import br.com.unisinos.es.t2.application.port.out.taskcomment.DeleteTaskCommentByIdPort;
import br.com.unisinos.es.t2.application.port.out.taskcomment.GetTaskCommentByIdPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class DeleteTaskCommentImpl implements DeleteTaskCommentService {

    private final GetAuthenticatedUserPort getAuthenticatedUserPort;
    private final GetTaskCommentByIdPort getTaskCommentByIdPort;
    private final DeleteTaskCommentByIdPort deleteTaskCommentByIdPort;

    @Override
    public void deleteComment(DeleteTaskCommentCommand command) {
        log.debug("Deleting comment id {} from task id {}", command.commentId(), command.taskId());
        User user = getAuthenticatedUserPort.getAuthenticatedUser().orElseThrow(NotAuthenticatedException::new);
        TaskComment comment = getTaskCommentByIdPort
                .getById(command.commentId())
                .orElseThrow(() -> new NotFoundException("Comment not found"));

        if (!comment.getTaskId().equals(command.taskId())) {
            throw new NotFoundException("Comment not found");
        }

        deleteTaskCommentByIdPort.deleteById(command.commentId());
        log.debug("Comment id {} deleted by user id {}", command.commentId(), user.getId());
    }
}
