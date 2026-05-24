package br.com.unisinos.es.t2.application.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.unisinos.es.t2.application.domain.exception.ClientException;
import br.com.unisinos.es.t2.application.domain.exception.NotFoundException;
import br.com.unisinos.es.t2.application.domain.model.Task;
import br.com.unisinos.es.t2.application.domain.model.TaskEvent;
import br.com.unisinos.es.t2.application.domain.model.TaskStatus;
import br.com.unisinos.es.t2.application.port.in.CreateTaskService.CreateTaskCommand;
import br.com.unisinos.es.t2.application.port.out.ExistsUserByIdPort;
import br.com.unisinos.es.t2.application.port.out.NotifyTaskEventPort;
import br.com.unisinos.es.t2.application.port.out.SaveTaskPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateTaskImplTest {

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private ExistsUserByIdPort existsUserByIdPort;

    @Mock
    private SaveTaskPort saveTaskPort;

    @Mock
    private NotifyTaskEventPort notifyTaskEventPort;

    @InjectMocks
    private CreateTaskImpl createTask;

    @Test
    void create_withoutAssignee_savesAsCreatedAndNotifiesCreatedOnly() {
        CreateTaskCommand command = new CreateTaskCommand("Title", "Desc", "creator-1", null);
        Task taskFromMapper = Task.builder()
                .title("Title")
                .description("Desc")
                .creatorId("creator-1")
                .build();
        Task savedTask = Task.builder()
                .id("task-1")
                .title("Title")
                .creatorId("creator-1")
                .status(TaskStatus.CREATED)
                .build();
        when(existsUserByIdPort.exists("creator-1")).thenReturn(true);
        when(taskMapper.toTask(command)).thenReturn(taskFromMapper);
        when(saveTaskPort.save(any())).thenReturn(savedTask);

        Task result = createTask.create(command);

        assertThat(result).isSameAs(savedTask);
        assertThat(taskFromMapper.getStatus()).isEqualTo(TaskStatus.CREATED);
        verify(notifyTaskEventPort).notify(TaskEvent.CREATED, savedTask);
        verify(notifyTaskEventPort, never()).notify(eq(TaskEvent.ASSIGNED), any());
    }

    @Test
    void create_withAssignee_savesAsAssignedAndNotifiesBoth() {
        CreateTaskCommand command = new CreateTaskCommand("T", null, "creator-1", "assignee-1");
        Task taskFromMapper = Task.builder()
                .title("T")
                .creatorId("creator-1")
                .assigneeId("assignee-1")
                .build();
        Task savedTask = Task.builder()
                .id("task-2")
                .title("T")
                .creatorId("creator-1")
                .assigneeId("assignee-1")
                .status(TaskStatus.ASSIGNED)
                .build();
        when(existsUserByIdPort.exists("creator-1")).thenReturn(true);
        when(existsUserByIdPort.exists("assignee-1")).thenReturn(true);
        when(taskMapper.toTask(command)).thenReturn(taskFromMapper);
        when(saveTaskPort.save(any())).thenReturn(savedTask);

        createTask.create(command);

        assertThat(taskFromMapper.getStatus()).isEqualTo(TaskStatus.ASSIGNED);
        verify(notifyTaskEventPort).notify(TaskEvent.CREATED, savedTask);
        verify(notifyTaskEventPort).notify(TaskEvent.ASSIGNED, savedTask);
    }

    @Test
    void create_withoutCreatorId_throwsClientException() {
        CreateTaskCommand command = new CreateTaskCommand("T", null, "  ", null);

        assertThatThrownBy(() -> createTask.create(command))
                .isInstanceOf(ClientException.class)
                .hasMessageContaining("creatorId");
        verify(saveTaskPort, never()).save(any());
        verify(notifyTaskEventPort, never()).notify(any(), any());
    }

    @Test
    void create_creatorNotFound_throwsNotFound() {
        CreateTaskCommand command = new CreateTaskCommand("T", null, "creator-x", null);
        when(existsUserByIdPort.exists("creator-x")).thenReturn(false);

        assertThatThrownBy(() -> createTask.create(command))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Creator");
        verify(saveTaskPort, never()).save(any());
        verify(notifyTaskEventPort, never()).notify(any(), any());
    }

    @Test
    void create_assigneeNotFound_throwsNotFound() {
        CreateTaskCommand command = new CreateTaskCommand("T", null, "creator-1", "ghost");
        when(existsUserByIdPort.exists("creator-1")).thenReturn(true);
        when(existsUserByIdPort.exists("ghost")).thenReturn(false);

        assertThatThrownBy(() -> createTask.create(command))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Assignee");
        verify(saveTaskPort, never()).save(any());
    }
}
