package br.com.unisinos.es.t2.application.domain.service;

import br.com.unisinos.es.t2.application.domain.exception.ClientException;
import br.com.unisinos.es.t2.application.domain.exception.NotFoundException;
import br.com.unisinos.es.t2.application.domain.model.Task;
import br.com.unisinos.es.t2.application.domain.model.TaskEvent;
import br.com.unisinos.es.t2.application.domain.model.TaskStatus;
import br.com.unisinos.es.t2.application.port.in.CreateTaskService;
import br.com.unisinos.es.t2.application.port.out.ExistsUserByIdPort;
import br.com.unisinos.es.t2.application.port.out.NotifyTaskEventPort;
import br.com.unisinos.es.t2.application.port.out.SaveTaskPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class CreateTaskImpl implements CreateTaskService {

    private final TaskMapper taskMapper;
    private final ExistsUserByIdPort existsUserByIdPort;
    private final SaveTaskPort saveTaskPort;
    private final NotifyTaskEventPort notifyTaskEventPort;

    @Override
    public Task create(CreateTaskCommand command) {
        if (command.creatorId() == null || command.creatorId().isBlank()) {
            throw new ClientException("creatorId is required");
        }
        if (!existsUserByIdPort.exists(command.creatorId())) {
            throw new NotFoundException("Creator user not found");
        }
        if (command.assigneeId() != null
                && !command.assigneeId().isBlank()
                && !existsUserByIdPort.exists(command.assigneeId())) {
            throw new NotFoundException("Assignee user not found");
        }

        Task task = taskMapper.toTask(command);
        boolean hasAssignee =
                command.assigneeId() != null && !command.assigneeId().isBlank();
        task.setStatus(hasAssignee ? TaskStatus.ASSIGNED : TaskStatus.CREATED);

        log.debug("Creating task '{}' by user {}", command.title(), command.creatorId());
        Task saved = saveTaskPort.save(task);

        notifyTaskEventPort.notify(TaskEvent.CREATED, saved);
        if (hasAssignee) {
            notifyTaskEventPort.notify(TaskEvent.ASSIGNED, saved);
        }
        return saved;
    }
}
