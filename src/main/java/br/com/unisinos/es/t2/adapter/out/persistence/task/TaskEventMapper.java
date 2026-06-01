package br.com.unisinos.es.t2.adapter.out.persistence.task;

import br.com.unisinos.es.t2.application.domain.model.TaskCreatedEvent;
import br.com.unisinos.es.t2.application.domain.model.TaskDeletedEvent;
import br.com.unisinos.es.t2.application.domain.model.TaskEvent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface TaskEventMapper {

    TaskCreatedEventEntity toEntity(TaskCreatedEvent taskCreatedEvent);

    TaskDeletedEventEntity toEntity(TaskDeletedEvent taskDeletedEvent);

    default TaskEventEntity toEntity(TaskEvent taskEvent) {
        return switch (taskEvent) {
            case TaskCreatedEvent createdEvent -> toEntity(createdEvent);
            case TaskDeletedEvent deletedEvent -> toEntity(deletedEvent);
        };
    }

    TaskCreatedEvent toDomain(TaskCreatedEventEntity taskCreatedEventEntity);

    TaskDeletedEvent toDomain(TaskDeletedEventEntity taskDeletedEventEntity);

    default TaskEvent toDomain(TaskEventEntity taskEventEntity) {
        return switch (taskEventEntity) {
            case TaskCreatedEventEntity createdEventEntity -> toDomain(createdEventEntity);
            case TaskDeletedEventEntity deletedEventEntity -> toDomain(deletedEventEntity);
        };
    }
}
