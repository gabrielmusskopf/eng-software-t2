package br.com.unisinos.es.t2.adapter.out.persistence.task;

import br.com.unisinos.es.t2.application.domain.model.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskEntity toTaskEntity(Task task);

    Task toTask(TaskEntity taskEntity);
}
