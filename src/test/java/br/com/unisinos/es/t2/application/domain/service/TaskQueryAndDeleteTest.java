package br.com.unisinos.es.t2.application.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.unisinos.es.t2.application.domain.exception.NotFoundException;
import br.com.unisinos.es.t2.application.domain.model.Task;
import br.com.unisinos.es.t2.application.port.in.DeleteTaskService.DeleteTaskCommand;
import br.com.unisinos.es.t2.application.port.in.GetTaskService.GetTaskCommand;
import br.com.unisinos.es.t2.application.port.out.DeleteTaskPort;
import br.com.unisinos.es.t2.application.port.out.ExistsTaskByIdPort;
import br.com.unisinos.es.t2.application.port.out.GetTaskByIdPort;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskQueryAndDeleteTest {

    @Mock
    private GetTaskByIdPort getTaskByIdPort;

    @Mock
    private ExistsTaskByIdPort existsTaskByIdPort;

    @Mock
    private DeleteTaskPort deleteTaskPort;

    @InjectMocks
    private GetTaskImpl getTask;

    @InjectMocks
    private DeleteTaskImpl deleteTask;

    @Test
    void get_returnsTask_whenFound() {
        Task task = Task.builder().id("t").title("T").build();
        when(getTaskByIdPort.getById("t")).thenReturn(Optional.of(task));

        Task result = getTask.get(new GetTaskCommand("t"));

        assertThat(result).isSameAs(task);
    }

    @Test
    void get_throwsNotFound_whenAbsent() {
        when(getTaskByIdPort.getById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> getTask.get(new GetTaskCommand("missing"))).isInstanceOf(NotFoundException.class);
    }

    @Test
    void delete_callsPort_whenExists() {
        when(existsTaskByIdPort.exists("t")).thenReturn(true);

        deleteTask.delete(new DeleteTaskCommand("t"));

        verify(deleteTaskPort).delete("t");
    }

    @Test
    void delete_throwsNotFound_whenMissing() {
        when(existsTaskByIdPort.exists("missing")).thenReturn(false);

        assertThatThrownBy(() -> deleteTask.delete(new DeleteTaskCommand("missing")))
                .isInstanceOf(NotFoundException.class);
        verify(deleteTaskPort, never()).delete("missing");
    }
}
