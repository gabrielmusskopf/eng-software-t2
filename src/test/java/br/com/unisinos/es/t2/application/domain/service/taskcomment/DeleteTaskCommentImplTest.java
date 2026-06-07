package br.com.unisinos.es.t2.application.domain.service.taskcomment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import br.com.unisinos.es.t2.EasyRandomExtension;
import br.com.unisinos.es.t2.application.domain.exception.NotAuthenticatedException;
import br.com.unisinos.es.t2.application.domain.exception.NotFoundException;
import br.com.unisinos.es.t2.application.domain.model.TaskComment;
import br.com.unisinos.es.t2.application.domain.model.User;
import br.com.unisinos.es.t2.application.port.in.auth.GetAuthenticatedUserPort;
import br.com.unisinos.es.t2.application.port.in.taskcomment.DeleteTaskCommentService;
import br.com.unisinos.es.t2.application.port.out.taskcomment.DeleteTaskCommentByIdPort;
import br.com.unisinos.es.t2.application.port.out.taskcomment.GetTaskCommentByIdPort;
import java.util.Optional;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(EasyRandomExtension.class)
class DeleteTaskCommentImplTest {

    @InjectMocks
    private DeleteTaskCommentImpl deleteTaskCommentImpl;

    @Mock
    private GetAuthenticatedUserPort getAuthenticatedUserPort;

    @Mock
    private GetTaskCommentByIdPort getTaskCommentByIdPort;

    @Mock
    private DeleteTaskCommentByIdPort deleteTaskCommentByIdPort;

    @Test
    void deleteCommentShouldThrowWhenUserNotAuthenticated() {
        when(getAuthenticatedUserPort.getAuthenticatedUser()).thenReturn(Optional.empty());

        DeleteTaskCommentService.DeleteTaskCommentCommand command =
                new DeleteTaskCommentService.DeleteTaskCommentCommand("task-id", "comment-id");

        assertThrows(NotAuthenticatedException.class, () -> deleteTaskCommentImpl.deleteComment(command));

        verify(getAuthenticatedUserPort).getAuthenticatedUser();
        verifyNoInteractions(getTaskCommentByIdPort, deleteTaskCommentByIdPort);
    }

    @Test
    void deleteCommentShouldThrowNotFoundWhenCommentDoesNotExist(EasyRandom easyRandom) {
        User user = easyRandom.nextObject(User.class);
        when(getAuthenticatedUserPort.getAuthenticatedUser()).thenReturn(Optional.of(user));
        when(getTaskCommentByIdPort.getById("non-existent-comment")).thenReturn(Optional.empty());

        DeleteTaskCommentService.DeleteTaskCommentCommand command =
                new DeleteTaskCommentService.DeleteTaskCommentCommand("task-id", "non-existent-comment");

        NotFoundException exception =
                assertThrows(NotFoundException.class, () -> deleteTaskCommentImpl.deleteComment(command));

        assertEquals("Comment not found", exception.getMessage());
        verifyNoInteractions(deleteTaskCommentByIdPort);
    }

    @Test
    void deleteCommentShouldThrowNotFoundWhenCommentBelongsToDifferentTask(EasyRandom easyRandom) {
        User user = easyRandom.nextObject(User.class);
        TaskComment comment = easyRandom.nextObject(TaskComment.class);
        comment.setTaskId("different-task-id");

        when(getAuthenticatedUserPort.getAuthenticatedUser()).thenReturn(Optional.of(user));
        when(getTaskCommentByIdPort.getById(comment.getId())).thenReturn(Optional.of(comment));

        DeleteTaskCommentService.DeleteTaskCommentCommand command =
                new DeleteTaskCommentService.DeleteTaskCommentCommand("requested-task-id", comment.getId());

        NotFoundException exception =
                assertThrows(NotFoundException.class, () -> deleteTaskCommentImpl.deleteComment(command));

        assertEquals("Comment not found", exception.getMessage());
        verifyNoInteractions(deleteTaskCommentByIdPort);
    }

    @Test
    void deleteCommentShouldSucceed(EasyRandom easyRandom) {
        User user = easyRandom.nextObject(User.class);
        TaskComment comment = easyRandom.nextObject(TaskComment.class);
        comment.setTaskId("task-id");

        when(getAuthenticatedUserPort.getAuthenticatedUser()).thenReturn(Optional.of(user));
        when(getTaskCommentByIdPort.getById(comment.getId())).thenReturn(Optional.of(comment));
        doNothing().when(deleteTaskCommentByIdPort).deleteById(comment.getId());

        DeleteTaskCommentService.DeleteTaskCommentCommand command =
                new DeleteTaskCommentService.DeleteTaskCommentCommand("task-id", comment.getId());

        deleteTaskCommentImpl.deleteComment(command);

        verify(deleteTaskCommentByIdPort).deleteById(comment.getId());
        verifyNoMoreInteractions(deleteTaskCommentByIdPort);
    }
}
