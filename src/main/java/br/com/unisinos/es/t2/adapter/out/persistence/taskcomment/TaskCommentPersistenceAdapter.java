package br.com.unisinos.es.t2.adapter.out.persistence.taskcomment;

import br.com.unisinos.es.t2.application.domain.model.TaskComment;
import br.com.unisinos.es.t2.application.port.out.taskcomment.CreateTaskCommentPort;
import br.com.unisinos.es.t2.application.port.out.taskcomment.DeleteTaskCommentByIdPort;
import br.com.unisinos.es.t2.application.port.out.taskcomment.GetTaskCommentByIdPort;
import br.com.unisinos.es.t2.application.port.out.taskcomment.GetTaskCommentsByTaskIdPort;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskCommentPersistenceAdapter
        implements CreateTaskCommentPort,
                GetTaskCommentsByTaskIdPort,
                GetTaskCommentByIdPort,
                DeleteTaskCommentByIdPort {

    private final TaskCommentRepository taskCommentRepository;
    private final TaskCommentMapper taskCommentMapper;

    @Override
    public TaskComment create(TaskComment comment) {
        TaskCommentEntity entity = taskCommentMapper.toEntity(comment);
        TaskCommentEntity saved = taskCommentRepository.save(entity);
        return taskCommentMapper.toTaskComment(saved);
    }

    @Override
    public List<TaskComment> getByTaskId(String taskId) {
        return taskCommentRepository.findByTaskId(taskId).stream()
                .map(taskCommentMapper::toTaskComment)
                .toList();
    }

    @Override
    public Optional<TaskComment> getById(String id) {
        return taskCommentRepository.findById(id).map(taskCommentMapper::toTaskComment);
    }

    @Override
    public void deleteById(String id) {
        taskCommentRepository.deleteById(id);
    }
}
