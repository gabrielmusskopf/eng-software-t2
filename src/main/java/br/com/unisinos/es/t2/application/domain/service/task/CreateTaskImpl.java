package br.com.unisinos.es.t2.application.domain.service.task;

import br.com.unisinos.es.t2.application.domain.exception.NotAuthenticatedException;
import br.com.unisinos.es.t2.application.domain.model.Task;
import br.com.unisinos.es.t2.application.domain.model.TaskCreatedEvent;
import br.com.unisinos.es.t2.application.domain.model.User;
import br.com.unisinos.es.t2.application.port.in.auth.GetAuthenticatedUserPort;
import br.com.unisinos.es.t2.application.port.in.task.CreateTaskService;
import br.com.unisinos.es.t2.application.port.out.task.CreateTaskEventPort;
import br.com.unisinos.es.t2.application.port.out.task.CreateTaskPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateTaskImpl implements CreateTaskService {

    private final GetAuthenticatedUserPort getAuthenticatedUserPort;
    private final CreateTaskPort createTaskPort;
    private final CreateTaskEventPort createTaskEventPort;

    @Override
    public Task createTask(CreateTaskCommand command) {
        User user = getAuthenticatedUserPort.getAuthenticatedUser().orElseThrow(NotAuthenticatedException::new);

        Task task = createTaskPort.createTask(new Task(command.title(), command.description(), user.getId()));
        log.debug("Task {} created with id {}", task.getTitle(), task.getId());

        createTaskEventPort.createTaskEvent(new TaskCreatedEvent(task));
        log.debug("TaskCreatedEvent created for task id {}", task.getId());

        return task;
    }
}
