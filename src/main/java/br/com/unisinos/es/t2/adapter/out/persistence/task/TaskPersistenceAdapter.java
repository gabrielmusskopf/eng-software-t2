package br.com.unisinos.es.t2.adapter.out.persistence.task;

import br.com.unisinos.es.t2.application.domain.model.Task;
import br.com.unisinos.es.t2.application.port.out.task.CreateTaskPort;
import br.com.unisinos.es.t2.application.port.out.task.DeleteTaskByIdPort;
import br.com.unisinos.es.t2.application.port.out.task.GetTaskByIdPort;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskPersistenceAdapter implements CreateTaskPort, GetTaskByIdPort, DeleteTaskByIdPort {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public Task createTask(Task task) {
        TaskEntity taskEntity = taskMapper.toTaskEntity(task);
        TaskEntity saved = taskRepository.save(taskEntity);
        return taskMapper.toTask(saved);
    }

    @Override
    public Optional<Task> getById(String id) {
        return taskRepository.findByIdAndDeletedFalse(id).map(taskMapper::toTask);
    }

    @Override
    public void deleteById(String id) {
        taskRepository.deleteById(id);
    }
}
