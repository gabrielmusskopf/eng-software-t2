package br.com.unisinos.es.t2.adapter.out.persistence.task;

import br.com.unisinos.es.t2.application.domain.model.TaskEvent;
import br.com.unisinos.es.t2.application.port.out.task.CreateTaskEventPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskEventPersistenceAdapter implements CreateTaskEventPort {

    private final TaskEventRepository taskEventRepository;
    private final TaskEventMapper taskEventMapper;

    @Override
    public TaskEvent createTaskEvent(TaskEvent event) {
        TaskEventEntity entity = taskEventMapper.toEntity(event);
        TaskEventEntity savedEntity = taskEventRepository.save(entity);
        return taskEventMapper.toDomain(savedEntity);
    }
}
