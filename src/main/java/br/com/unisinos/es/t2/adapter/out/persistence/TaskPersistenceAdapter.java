package br.com.unisinos.es.t2.adapter.out.persistence;

import br.com.unisinos.es.t2.application.domain.model.Task;
import br.com.unisinos.es.t2.application.port.out.DeleteTaskPort;
import br.com.unisinos.es.t2.application.port.out.ExistsTaskByIdPort;
import br.com.unisinos.es.t2.application.port.out.GetTaskByIdPort;
import br.com.unisinos.es.t2.application.port.out.ListTasksByAssigneePort;
import br.com.unisinos.es.t2.application.port.out.SaveTaskPort;
import br.com.unisinos.es.t2.application.port.out.UpdateTaskPort;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class TaskPersistenceAdapter
        implements SaveTaskPort,
                GetTaskByIdPort,
                UpdateTaskPort,
                DeleteTaskPort,
                ListTasksByAssigneePort,
                ExistsTaskByIdPort {

    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;

    @Override
    public Task save(Task task) {
        TaskEntity savedTask = taskRepository.save(taskMapper.toEntity(task));
        task.setId(savedTask.getId());
        task.setCreatedAt(savedTask.getCreatedAt());
        task.setUpdatedAt(savedTask.getUpdatedAt());
        return task;
    }

    @Override
    public Optional<Task> getById(String id) {
        return taskRepository.findByIdAndDeletedFalse(id).map(taskMapper::toDomain);
    }

    @Override
    public void update(Task task) {
        TaskEntity updated = taskRepository.save(taskMapper.toEntity(task));
        task.setUpdatedAt(updated.getUpdatedAt());
    }

    @Override
    public void delete(String id) {
        taskRepository.deleteById(id);
    }

    @Override
    public List<Task> listByAssigneeId(String assigneeId) {
        return taskRepository.findAllByAssigneeIdAndDeletedFalse(assigneeId).stream()
                .map(taskMapper::toDomain)
                .toList();
    }

    @Override
    public boolean exists(String id) {
        return taskRepository.existsByIdAndDeletedFalse(id);
    }
}
