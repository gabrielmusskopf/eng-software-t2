package br.com.unisinos.es.t2.adapter.out.persistence.task;

import br.com.unisinos.es.t2.application.domain.model.Task;
import br.com.unisinos.es.t2.application.port.out.task.CreateTaskPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskPersistenceAdapter implements CreateTaskPort {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public Task createTask(Task task) {
        TaskEntity taskEntity = taskMapper.toTaskEntity(task);
        TaskEntity saved = taskRepository.save(taskEntity);
        task.setId(saved.getId());
        task.setCreatedAt(saved.getCreatedAt());
        task.setUpdatedAt(saved.getUpdatedAt());
        return taskMapper.toTask(saved);
    }
}
