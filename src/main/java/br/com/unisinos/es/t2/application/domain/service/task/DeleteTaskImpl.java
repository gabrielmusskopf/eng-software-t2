package br.com.unisinos.es.t2.application.domain.service.task;

import br.com.unisinos.es.t2.application.domain.exception.NotAuthenticatedException;
import br.com.unisinos.es.t2.application.domain.exception.NotFoundException;
import br.com.unisinos.es.t2.application.domain.exception.ServerException;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class DeleteTaskImpl implements DeleteTaskService {

    private final GetAuthenticatedUserPort getAuthenticatedUserPort;
    private final GetTaskByIdPort getTaskByIdPort;
    private final GetUserByIdPort getUserByIdPort;
    private final DeleteTaskByIdPort deleteTaskByIdPort;
    private final CreateTaskEventPort createTaskEventPort;
    private final PublishEventPort publishEventPort;

    @Override
    public void deleteTask(DeleteTaskCommand command) {
        log.debug("Deleting task with id {}", command.id());
        User authUser = getAuthenticatedUserPort.getAuthenticatedUser().orElseThrow(NotAuthenticatedException::new);
        Task task = getTaskByIdPort.getById(command.id()).orElseThrow(() -> new NotFoundException("Task not found"));
        User assignedUser = getUserByIdPort
                .getById(task.getUserId())
                .orElseThrow(() -> new ServerException("User not found for task " + task.getId()));

        deleteTaskByIdPort.deleteById(task.getId());

        TaskDeletedEvent taskDeletedEvent = new TaskDeletedEvent(task, authUser, assignedUser);
        createTaskEventPort.createTaskEvent(taskDeletedEvent);
        log.debug("TaskDeletedEvent created for task id {}", task.getId());

        publishEventPort.publish(taskDeletedEvent);
    }
}
