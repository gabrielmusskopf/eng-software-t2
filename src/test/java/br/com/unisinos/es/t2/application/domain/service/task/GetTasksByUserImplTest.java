package br.com.unisinos.es.t2.application.domain.service.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import br.com.unisinos.es.t2.EasyRandomExtension;
import br.com.unisinos.es.t2.application.domain.exception.NotFoundException;
import br.com.unisinos.es.t2.application.domain.model.Task;
import br.com.unisinos.es.t2.application.port.in.task.GetTasksByUserService;
import br.com.unisinos.es.t2.application.port.out.task.GetTasksByUserIdPort;
import br.com.unisinos.es.t2.application.port.out.user.ExistsUserByIdPort;
import java.util.List;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(EasyRandomExtension.class)
class GetTasksByUserImplTest {

    @InjectMocks
    private GetTasksByUserImpl getTasksByUserImpl;

    @Mock
    private ExistsUserByIdPort existsUserByIdPort;

    @Mock
    private GetTasksByUserIdPort getTasksByUserIdPort;

    @Test
    void getByAssignedUserShouldThrowNotFoundWhenUserDoesNotExist() {
        String userId = "non-existent-id";
        when(existsUserByIdPort.exists(userId)).thenReturn(false);

        GetTasksByUserService.GetTasksByUserCommand command = new GetTasksByUserService.GetTasksByUserCommand(userId);

        NotFoundException exception =
                assertThrows(NotFoundException.class, () -> getTasksByUserImpl.getByAssignedUser(command));

        assertEquals("User not found", exception.getMessage());

        verify(existsUserByIdPort).exists(userId);
        verifyNoInteractions(getTasksByUserIdPort);
    }

    @Test
    void getByAssignedUserShouldReturnEmptyListWhenUserHasNoTasks() {
        String userId = "user-with-no-tasks";
        when(existsUserByIdPort.exists(userId)).thenReturn(true);
        when(getTasksByUserIdPort.getByUserId(userId)).thenReturn(List.of());

        GetTasksByUserService.GetTasksByUserCommand command = new GetTasksByUserService.GetTasksByUserCommand(userId);

        List<Task> result = getTasksByUserImpl.getByAssignedUser(command);

        assertEquals(0, result.size());

        verify(existsUserByIdPort).exists(userId);
        verify(getTasksByUserIdPort).getByUserId(userId);
        verifyNoMoreInteractions(existsUserByIdPort, getTasksByUserIdPort);
    }

    @Test
    void getByAssignedUserShouldReturnTasksWhenUserExists(EasyRandom easyRandom) {
        String userId = "existing-user-id";
        Task task1 = easyRandom.nextObject(Task.class);
        Task task2 = easyRandom.nextObject(Task.class);
        List<Task> expectedTasks = List.of(task1, task2);

        when(existsUserByIdPort.exists(userId)).thenReturn(true);
        when(getTasksByUserIdPort.getByUserId(userId)).thenReturn(expectedTasks);

        GetTasksByUserService.GetTasksByUserCommand command = new GetTasksByUserService.GetTasksByUserCommand(userId);

        List<Task> result = getTasksByUserImpl.getByAssignedUser(command);

        assertEquals(2, result.size());
        assertEquals(task1.getId(), result.get(0).getId());
        assertEquals(task2.getId(), result.get(1).getId());

        verify(existsUserByIdPort).exists(userId);
        verify(getTasksByUserIdPort).getByUserId(userId);
        verifyNoMoreInteractions(existsUserByIdPort, getTasksByUserIdPort);
    }
}
