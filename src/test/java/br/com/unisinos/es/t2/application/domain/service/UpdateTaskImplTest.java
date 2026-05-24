package br.com.unisinos.es.t2.application.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.unisinos.es.t2.application.domain.exception.NotFoundException;
import br.com.unisinos.es.t2.application.domain.model.Task;
import br.com.unisinos.es.t2.application.domain.model.TaskEvent;
import br.com.unisinos.es.t2.application.domain.model.TaskStatus;
import br.com.unisinos.es.t2.application.port.in.UpdateTaskService.UpdateTaskCommand;
import br.com.unisinos.es.t2.application.port.out.ExistsUserByIdPort;
import br.com.unisinos.es.t2.application.port.out.GetTaskByIdPort;
import br.com.unisinos.es.t2.application.port.out.NotifyTaskEventPort;
import br.com.unisinos.es.t2.application.port.out.UpdateTaskPort;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateTaskImplTest {

    @Mock
    private GetTaskByIdPort getTaskByIdPort;

    @Mock
    private ExistsUserByIdPort existsUserByIdPort;

    @Mock
    private UpdateTaskPort updateTaskPort;

    @Mock
    private NotifyTaskEventPort notifyTaskEventPort;

    @InjectMocks
    private UpdateTaskImpl updateTask;

    @Test
    void update_titleAndDescriptionOnly_doesNotNotify() {
        Task existing = Task.builder()
                .id("t-1")
                .title("old")
                .status(TaskStatus.CREATED)
                .creatorId("c")
                .build();
        when(getTaskByIdPort.getById("t-1")).thenReturn(Optional.of(existing));

        updateTask.update(new UpdateTaskCommand("t-1", "new", "desc", null, null));

        assertThat(existing.getTitle()).isEqualTo("new");
        assertThat(existing.getDescription()).isEqualTo("desc");
        verify(updateTaskPort).update(existing);
        verify(notifyTaskEventPort, never()).notify(any(), any());
    }

    @Test
    void update_assignNewUser_setsStatusAssignedAndNotifies() {
        Task existing = Task.builder()
                .id("t-1")
                .title("X")
                .status(TaskStatus.CREATED)
                .creatorId("c")
                .build();
        when(getTaskByIdPort.getById("t-1")).thenReturn(Optional.of(existing));
        when(existsUserByIdPort.exists("u-1")).thenReturn(true);

        updateTask.update(new UpdateTaskCommand("t-1", null, null, null, "u-1"));

        assertThat(existing.getAssigneeId()).isEqualTo("u-1");
        assertThat(existing.getStatus()).isEqualTo(TaskStatus.ASSIGNED);
        verify(notifyTaskEventPort).notify(TaskEvent.ASSIGNED, existing);
        verify(notifyTaskEventPort, never()).notify(eq(TaskEvent.FINISHED), any());
    }

    @Test
    void update_assigneeNotFound_throws() {
        Task existing =
                Task.builder().id("t-1").title("X").status(TaskStatus.CREATED).build();
        when(getTaskByIdPort.getById("t-1")).thenReturn(Optional.of(existing));
        when(existsUserByIdPort.exists("ghost")).thenReturn(false);

        assertThatThrownBy(() -> updateTask.update(new UpdateTaskCommand("t-1", null, null, null, "ghost")))
                .isInstanceOf(NotFoundException.class);
        verify(updateTaskPort, never()).update(any());
        verify(notifyTaskEventPort, never()).notify(any(), any());
    }

    @Test
    void update_finishStatus_notifiesFinished() {
        Task existing = Task.builder()
                .id("t-1")
                .title("X")
                .status(TaskStatus.ASSIGNED)
                .assigneeId("u")
                .creatorId("c")
                .build();
        when(getTaskByIdPort.getById("t-1")).thenReturn(Optional.of(existing));

        updateTask.update(new UpdateTaskCommand("t-1", null, null, TaskStatus.FINISHED, null));

        assertThat(existing.getStatus()).isEqualTo(TaskStatus.FINISHED);
        verify(notifyTaskEventPort).notify(TaskEvent.FINISHED, existing);
    }

    @Test
    void update_taskNotFound_throws() {
        when(getTaskByIdPort.getById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> updateTask.update(new UpdateTaskCommand("missing", "T", null, null, null)))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void update_sameAssignee_doesNotReNotify() {
        Task existing = Task.builder()
                .id("t-1")
                .title("X")
                .status(TaskStatus.ASSIGNED)
                .assigneeId("u-1")
                .build();
        when(getTaskByIdPort.getById("t-1")).thenReturn(Optional.of(existing));

        updateTask.update(new UpdateTaskCommand("t-1", null, null, null, "u-1"));

        verify(notifyTaskEventPort, never()).notify(any(), any());
    }
}
