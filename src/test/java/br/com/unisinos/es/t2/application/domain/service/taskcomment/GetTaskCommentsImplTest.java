package br.com.unisinos.es.t2.application.domain.service.taskcomment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import br.com.unisinos.es.t2.EasyRandomExtension;
import br.com.unisinos.es.t2.application.domain.exception.NotFoundException;
import br.com.unisinos.es.t2.application.domain.model.Task;
import br.com.unisinos.es.t2.application.domain.model.TaskComment;
import br.com.unisinos.es.t2.application.port.in.taskcomment.GetTaskCommentsService;
import br.com.unisinos.es.t2.application.port.out.task.GetTaskByIdPort;
import br.com.unisinos.es.t2.application.port.out.taskcomment.GetTaskCommentsByTaskIdPort;
import java.util.List;
import java.util.Optional;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(EasyRandomExtension.class)
class GetTaskCommentsImplTest {

    @InjectMocks
    private GetTaskCommentsImpl getTaskCommentsImpl;

    @Mock
    private GetTaskByIdPort getTaskByIdPort;

    @Mock
    private GetTaskCommentsByTaskIdPort getTaskCommentsByTaskIdPort;

    @Test
    void getCommentsShouldThrowNotFoundWhenTaskDoesNotExist() {
        when(getTaskByIdPort.getById("non-existent-task")).thenReturn(Optional.empty());

        GetTaskCommentsService.GetTaskCommentsCommand command =
                new GetTaskCommentsService.GetTaskCommentsCommand("non-existent-task");

        NotFoundException exception =
                assertThrows(NotFoundException.class, () -> getTaskCommentsImpl.getComments(command));

        assertEquals("Task not found", exception.getMessage());
        verifyNoInteractions(getTaskCommentsByTaskIdPort);
    }

    @Test
    void getCommentsShouldReturnEmptyListWhenNoComments(EasyRandom easyRandom) {
        Task task = easyRandom.nextObject(Task.class);
        when(getTaskByIdPort.getById(task.getId())).thenReturn(Optional.of(task));
        when(getTaskCommentsByTaskIdPort.getByTaskId(task.getId())).thenReturn(List.of());

        GetTaskCommentsService.GetTaskCommentsCommand command =
                new GetTaskCommentsService.GetTaskCommentsCommand(task.getId());

        List<TaskComment> result = getTaskCommentsImpl.getComments(command);

        assertEquals(0, result.size());
        verify(getTaskCommentsByTaskIdPort).getByTaskId(task.getId());
        verifyNoMoreInteractions(getTaskCommentsByTaskIdPort);
    }

    @Test
    void getCommentsShouldReturnComments(EasyRandom easyRandom) {
        Task task = easyRandom.nextObject(Task.class);
        TaskComment c1 = easyRandom.nextObject(TaskComment.class);
        TaskComment c2 = easyRandom.nextObject(TaskComment.class);
        List<TaskComment> expected = List.of(c1, c2);

        when(getTaskByIdPort.getById(task.getId())).thenReturn(Optional.of(task));
        when(getTaskCommentsByTaskIdPort.getByTaskId(task.getId())).thenReturn(expected);

        GetTaskCommentsService.GetTaskCommentsCommand command =
                new GetTaskCommentsService.GetTaskCommentsCommand(task.getId());

        List<TaskComment> result = getTaskCommentsImpl.getComments(command);

        assertEquals(2, result.size());
        assertEquals(c1.getId(), result.get(0).getId());
        assertEquals(c2.getId(), result.get(1).getId());

        verify(getTaskCommentsByTaskIdPort).getByTaskId(task.getId());
    }
}
