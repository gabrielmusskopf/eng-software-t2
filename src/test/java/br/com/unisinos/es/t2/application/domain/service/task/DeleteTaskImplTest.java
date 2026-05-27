package br.com.unisinos.es.t2.application.domain.service.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import br.com.unisinos.es.t2.EasyRandomExtension;
import br.com.unisinos.es.t2.application.domain.exception.NotAuthenticatedException;
import br.com.unisinos.es.t2.application.domain.exception.NotFoundException;
import br.com.unisinos.es.t2.application.domain.model.Task;
import br.com.unisinos.es.t2.application.domain.model.TaskDeletedEvent;
import br.com.unisinos.es.t2.application.domain.model.User;
import br.com.unisinos.es.t2.application.port.in.auth.GetAuthenticatedUserPort;
import br.com.unisinos.es.t2.application.port.in.task.DeleteTaskService;
import br.com.unisinos.es.t2.application.port.out.event.PublishEventPort;
import br.com.unisinos.es.t2.application.port.out.task.CreateTaskEventPort;
import br.com.unisinos.es.t2.application.port.out.task.DeleteTaskByIdPort;
import br.com.unisinos.es.t2.application.port.out.task.GetTaskByIdPort;
import br.com.unisinos.es.t2.application.port.out.user.GetUserByIdPort;
import java.util.Optional;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(EasyRandomExtension.class)
class DeleteTaskImplTest {

    @InjectMocks
    private DeleteTaskImpl deleteTaskImpl;

    @Mock
    private GetAuthenticatedUserPort getAuthenticatedUserPort;

    @Mock
    private GetTaskByIdPort getTaskByIdPort;

    @Mock
    private GetUserByIdPort getUserByIdPort;

    @Mock
    private DeleteTaskByIdPort deleteTaskByIdPort;

    @Mock
    private CreateTaskEventPort createTaskEventPort;

    @Mock
    private PublishEventPort publishEventPort;

    @Test
    void deleteTaskShouldErrorWhenUserNotAuthenticated() {
        when(getAuthenticatedUserPort.getAuthenticatedUser()).thenReturn(Optional.empty());

        DeleteTaskService.DeleteTaskCommand command = new DeleteTaskService.DeleteTaskCommand("any-id");

        assertThrows(NotAuthenticatedException.class, () -> deleteTaskImpl.deleteTask(command));

        verify(getAuthenticatedUserPort).getAuthenticatedUser();
        verifyNoMoreInteractions(getAuthenticatedUserPort);
    }

    @Test
    void deleteTaskShouldErrorWhenTaskNotFound(EasyRandom easyRandom) {
        User user = easyRandom.nextObject(User.class);
        when(getAuthenticatedUserPort.getAuthenticatedUser()).thenReturn(Optional.of(user));
        when(getTaskByIdPort.getById("any-id")).thenReturn(Optional.empty());

        DeleteTaskService.DeleteTaskCommand command = new DeleteTaskService.DeleteTaskCommand("any-id");

        NotFoundException exception = assertThrows(NotFoundException.class, () -> deleteTaskImpl.deleteTask(command));

        assertEquals("Task not found", exception.getMessage());

        verify(getTaskByIdPort).getById("any-id");
        verifyNoMoreInteractions(getTaskByIdPort);
    }

    @Test
    void deleteTaskShouldSucceed(EasyRandom easyRandom) {
        User authenticatedUser = easyRandom.nextObject(User.class);
        Task task = easyRandom.nextObject(Task.class);
        User taskUser = easyRandom.nextObject(User.class);

        when(getAuthenticatedUserPort.getAuthenticatedUser()).thenReturn(Optional.of(authenticatedUser));
        when(getTaskByIdPort.getById(task.getId())).thenReturn(Optional.of(task));
        when(getUserByIdPort.getById(task.getUserId())).thenReturn(Optional.of(taskUser));
        doNothing().when(deleteTaskByIdPort).deleteById(task.getId());
        when(createTaskEventPort.createTaskEvent(any(TaskDeletedEvent.class)))
                .thenReturn(new TaskDeletedEvent(task, authenticatedUser, taskUser));
        doNothing().when(publishEventPort).publish(any(TaskDeletedEvent.class));

        DeleteTaskService.DeleteTaskCommand command = new DeleteTaskService.DeleteTaskCommand(task.getId());
        deleteTaskImpl.deleteTask(command);

        verify(deleteTaskByIdPort).deleteById(task.getId());
        verify(createTaskEventPort).createTaskEvent(any(TaskDeletedEvent.class));
        verify(publishEventPort).publish(any(TaskDeletedEvent.class));
        verifyNoMoreInteractions(deleteTaskByIdPort, createTaskEventPort, publishEventPort);
    }
}
