package br.com.unisinos.es.t2.application.domain.service.taskcomment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import br.com.unisinos.es.t2.EasyRandomExtension;
import br.com.unisinos.es.t2.application.domain.exception.NotAuthenticatedException;
import br.com.unisinos.es.t2.application.domain.exception.NotFoundException;
import br.com.unisinos.es.t2.application.domain.model.Task;
import br.com.unisinos.es.t2.application.domain.model.TaskComment;
import br.com.unisinos.es.t2.application.domain.model.User;
import br.com.unisinos.es.t2.application.port.in.auth.GetAuthenticatedUserPort;
import br.com.unisinos.es.t2.application.port.in.taskcomment.CreateTaskCommentService;
import br.com.unisinos.es.t2.application.port.out.task.GetTaskByIdPort;
import br.com.unisinos.es.t2.application.port.out.taskcomment.CreateTaskCommentPort;
import java.util.Optional;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(EasyRandomExtension.class)
class CreateTaskCommentImplTest {

    @InjectMocks
    private CreateTaskCommentImpl createTaskCommentImpl;

    @Mock
    private GetAuthenticatedUserPort getAuthenticatedUserPort;

    @Mock
    private GetTaskByIdPort getTaskByIdPort;

    @Mock
    private CreateTaskCommentPort createTaskCommentPort;

    @Test
    void createCommentShouldThrowWhenUserNotAuthenticated() {
        when(getAuthenticatedUserPort.getAuthenticatedUser()).thenReturn(Optional.empty());

        CreateTaskCommentService.CreateTaskCommentCommand command =
                new CreateTaskCommentService.CreateTaskCommentCommand("task-id", "some content");

        assertThrows(NotAuthenticatedException.class, () -> createTaskCommentImpl.createComment(command));

        verify(getAuthenticatedUserPort).getAuthenticatedUser();
        verifyNoInteractions(getTaskByIdPort, createTaskCommentPort);
    }

    @Test
    void createCommentShouldThrowNotFoundWhenTaskDoesNotExist(EasyRandom easyRandom) {
        User user = easyRandom.nextObject(User.class);
        when(getAuthenticatedUserPort.getAuthenticatedUser()).thenReturn(Optional.of(user));
        when(getTaskByIdPort.getById("non-existent-task")).thenReturn(Optional.empty());

        CreateTaskCommentService.CreateTaskCommentCommand command =
                new CreateTaskCommentService.CreateTaskCommentCommand("non-existent-task", "some content");

        NotFoundException exception =
                assertThrows(NotFoundException.class, () -> createTaskCommentImpl.createComment(command));

        assertEquals("Task not found", exception.getMessage());
        verifyNoInteractions(createTaskCommentPort);
    }

    @Test
    void createCommentShouldSucceed(EasyRandom easyRandom) {
        User user = easyRandom.nextObject(User.class);
        Task task = easyRandom.nextObject(Task.class);
        TaskComment savedComment = easyRandom.nextObject(TaskComment.class);

        when(getAuthenticatedUserPort.getAuthenticatedUser()).thenReturn(Optional.of(user));
        when(getTaskByIdPort.getById(task.getId())).thenReturn(Optional.of(task));
        when(createTaskCommentPort.create(any(TaskComment.class))).thenReturn(savedComment);

        CreateTaskCommentService.CreateTaskCommentCommand command =
                new CreateTaskCommentService.CreateTaskCommentCommand(task.getId(), "some content");

        TaskComment result = createTaskCommentImpl.createComment(command);

        assertEquals(savedComment.getId(), result.getId());
        assertEquals(savedComment.getContent(), result.getContent());

        verify(createTaskCommentPort).create(any(TaskComment.class));
        verifyNoMoreInteractions(createTaskCommentPort);
    }
}
