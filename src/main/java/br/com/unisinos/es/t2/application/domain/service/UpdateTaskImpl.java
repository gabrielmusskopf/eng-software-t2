package br.com.unisinos.es.t2.application.domain.service;

import br.com.unisinos.es.t2.application.domain.exception.NotFoundException;
import br.com.unisinos.es.t2.application.domain.model.Task;
import br.com.unisinos.es.t2.application.domain.model.TaskEvent;
import br.com.unisinos.es.t2.application.domain.model.TaskStatus;
import br.com.unisinos.es.t2.application.port.in.UpdateTaskService;
import br.com.unisinos.es.t2.application.port.out.ExistsUserByIdPort;
import br.com.unisinos.es.t2.application.port.out.GetTaskByIdPort;
import br.com.unisinos.es.t2.application.port.out.NotifyTaskEventPort;
import br.com.unisinos.es.t2.application.port.out.UpdateTaskPort;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class UpdateTaskImpl implements UpdateTaskService {

    private final GetTaskByIdPort getTaskByIdPort;
    private final ExistsUserByIdPort existsUserByIdPort;
    private final UpdateTaskPort updateTaskPort;
    private final NotifyTaskEventPort notifyTaskEventPort;

    @Override
    public void update(UpdateTaskCommand command) {
        Task task = getTaskByIdPort.getById(command.id()).orElseThrow(() -> new NotFoundException("Task not found"));

        boolean assigneeChanged = false;
        boolean finishedNow = false;

        if (command.title() != null) {
            task.setTitle(command.title());
        }
        if (command.description() != null) {
            task.setDescription(command.description());
        }
        if (command.assigneeId() != null && !Objects.equals(command.assigneeId(), task.getAssigneeId())) {
            if (!command.assigneeId().isBlank() && !existsUserByIdPort.exists(command.assigneeId())) {
                throw new NotFoundException("Assignee user not found");
            }
            task.setAssigneeId(command.assigneeId().isBlank() ? null : command.assigneeId());
            assigneeChanged = task.getAssigneeId() != null;
            if (assigneeChanged && task.getStatus() == TaskStatus.CREATED) {
                task.setStatus(TaskStatus.ASSIGNED);
            }
        }
        if (command.status() != null && command.status() != task.getStatus()) {
            log.debug("Updating task {} status from {} to {}", task.getId(), task.getStatus(), command.status());
            task.setStatus(command.status());
            finishedNow = command.status() == TaskStatus.FINISHED;
        }

        updateTaskPort.update(task);

        if (assigneeChanged) {
            notifyTaskEventPort.notify(TaskEvent.ASSIGNED, task);
        }
        if (finishedNow) {
            notifyTaskEventPort.notify(TaskEvent.FINISHED, task);
        }
    }
}
