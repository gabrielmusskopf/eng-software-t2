package br.com.unisinos.es.t2.application.domain.service.task;

import br.com.unisinos.es.t2.application.domain.exception.NotAuthenticatedException;
import br.com.unisinos.es.t2.application.domain.exception.NotFoundException;
import br.com.unisinos.es.t2.application.domain.exception.ServerException;
import br.com.unisinos.es.t2.application.domain.model.Task;
import br.com.unisinos.es.t2.application.domain.model.TaskDescriptionChangedEvent;
import br.com.unisinos.es.t2.application.domain.model.TaskEvent;
import br.com.unisinos.es.t2.application.domain.model.TaskReassignedEvent;
import br.com.unisinos.es.t2.application.domain.model.TaskStatusChangedEvent;
import br.com.unisinos.es.t2.application.domain.model.TaskTitleChangedEvent;
import br.com.unisinos.es.t2.application.domain.model.TaskUpdatedEvent;
import br.com.unisinos.es.t2.application.domain.model.User;
import br.com.unisinos.es.t2.application.port.in.auth.GetAuthenticatedUserPort;
import br.com.unisinos.es.t2.application.port.in.task.UpdateTaskService;
import br.com.unisinos.es.t2.application.port.out.event.PublishEventPort;
import br.com.unisinos.es.t2.application.port.out.task.CreateTaskEventPort;
import br.com.unisinos.es.t2.application.port.out.task.CreateTaskPort;
import br.com.unisinos.es.t2.application.port.out.task.GetTaskByIdPort;
import br.com.unisinos.es.t2.application.port.out.user.GetUserByIdPort;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class UpdateTaskImpl implements UpdateTaskService {

    private final GetAuthenticatedUserPort getAuthenticatedUserPort;
    private final GetTaskByIdPort getTaskByIdPort;
    private final GetUserByIdPort getUserByIdPort;
    private final CreateTaskPort createTaskPort;
    private final CreateTaskEventPort createTaskEventPort;
    private final PublishEventPort publishEventPort;

    @Override
    public Task updateTask(UpdateTaskCommand command) {
        log.debug("Updating task with id {}", command.taskId());

        User authUser = getAuthenticatedUserPort.getAuthenticatedUser().orElseThrow(NotAuthenticatedException::new);
        Task task =
                getTaskByIdPort.getById(command.taskId()).orElseThrow(() -> new NotFoundException("Task not found"));
        User oldAssignee = getUserByIdPort
                .getById(task.getUserId())
                .orElseThrow(() -> new ServerException("Current assignee not found"));
        User newAssignee = getUserByIdPort
                .getById(command.assigneeId())
                .orElseThrow(() -> new NotFoundException("Assignee not found"));

        boolean taskChanged = false;
        List<TaskEvent> events = new ArrayList<>();

        if (!oldAssignee.getId().equals(newAssignee.getId())) {
            log.debug(
                    "Reassigning task id {} from user id {} to user id {}",
                    task.getId(),
                    oldAssignee.getId(),
                    newAssignee.getId());
            events.add(new TaskReassignedEvent(task, authUser, oldAssignee, newAssignee));
            task.setUserId(newAssignee.getId());
            taskChanged = true;
        }
        if (!task.getTitle().equals(command.title())) {
            log.debug("Updating title of task id {} from '{}' to '{}'", task.getId(), task.getTitle(), command.title());
            events.add(new TaskTitleChangedEvent(task, authUser, newAssignee, task.getTitle()));
            task.setTitle(command.title());
            taskChanged = true;
        }
        if (!task.getDescription().equals(command.description())) {
            log.debug(
                    "Updating description of task id {} from '{}' to '{}'",
                    task.getId(),
                    task.getDescription(),
                    command.description());
            events.add(new TaskDescriptionChangedEvent(task, authUser, newAssignee, task.getDescription()));
            task.setDescription(command.description());
            taskChanged = true;
        }
        if (!task.getStatus().equals(command.status())) {
            log.debug(
                    "Updating status of task id {} from '{}' to '{}'",
                    task.getId(),
                    task.getStatus(),
                    command.status());
            events.add(new TaskStatusChangedEvent(task, authUser, task.getStatus()));
            task.setStatus(command.status());
            taskChanged = true;
        }

        if (!taskChanged) {
            log.debug("No changes detected for task with id {}, skipping update", command.taskId());
            return task;
        }

        Task newTask = createTaskPort.createTask(task);

        TaskUpdatedEvent taskUpdatedEvent = new TaskUpdatedEvent(newTask, authUser, newAssignee, events);
        createTaskEventPort.createTaskEvent(taskUpdatedEvent);
        publishEventPort.publish(taskUpdatedEvent);

        return newTask;
    }
}
