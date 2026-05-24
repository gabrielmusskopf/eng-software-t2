package br.com.unisinos.es.t2.application.domain.service.task;

import br.com.unisinos.es.t2.application.domain.exception.ClientException;
import br.com.unisinos.es.t2.application.domain.model.Task;
import br.com.unisinos.es.t2.application.domain.model.TaskCreatedEvent;
import br.com.unisinos.es.t2.application.port.in.task.CreateTaskService;
import br.com.unisinos.es.t2.application.port.out.task.CreateTaskEventPort;
import br.com.unisinos.es.t2.application.port.out.task.CreateTaskPort;
import br.com.unisinos.es.t2.application.port.out.user.ExistsUserByIdPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateTaskImpl implements CreateTaskService {

    private final ExistsUserByIdPort existsUserByIdPort;
    private final CreateTaskPort createTaskPort;
    private final CreateTaskEventPort createTaskEventPort;

    @Override
    public Task createTask(CreateTaskCommand command) {
        if (!existsUserByIdPort.exists(command.userId())) {
            log.error("User with id {} does not exist", command.userId());
            throw new ClientException("User not found");
        }

        Task task = createTaskPort.createTask(new Task(command.title(), command.description(), command.userId()));
        log.debug("Task {} created with id {}", task.getTitle(), task.getId());

        createTaskEventPort.createTaskEvent(new TaskCreatedEvent(task));
        log.debug("TaskCreatedEvent created for task id {}", task.getId());

        return task;
    }
}
