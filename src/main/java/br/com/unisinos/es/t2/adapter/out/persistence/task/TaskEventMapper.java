package br.com.unisinos.es.t2.adapter.out.persistence.task;

import br.com.unisinos.es.t2.application.domain.model.TaskCreatedEvent;
import br.com.unisinos.es.t2.application.domain.model.TaskDeletedEvent;
import br.com.unisinos.es.t2.application.domain.model.TaskDescriptionChangedEvent;
import br.com.unisinos.es.t2.application.domain.model.TaskEvent;
import br.com.unisinos.es.t2.application.domain.model.TaskReassignedEvent;
import br.com.unisinos.es.t2.application.domain.model.TaskStatusChangedEvent;
import br.com.unisinos.es.t2.application.domain.model.TaskTitleChangedEvent;
import br.com.unisinos.es.t2.application.domain.model.TaskUpdatedEvent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface TaskEventMapper {

    TaskCreatedEventEntity toEntity(TaskCreatedEvent taskCreatedEvent);

    TaskDeletedEventEntity toEntity(TaskDeletedEvent taskDeletedEvent);

    TaskReassignedEventEntity toEntity(TaskReassignedEvent taskReassignedEvent);

    TaskUpdatedEventEntity toEntity(TaskUpdatedEvent taskUpdatedEvent);

    TaskTitleChangedEventEntity toEntity(TaskTitleChangedEvent taskTitleChangedEvent);

    TaskDescriptionChangedEventEntity toEntity(TaskDescriptionChangedEvent taskDescriptionChangedEvent);

    TaskStatusChangedEventEntity toEntity(TaskStatusChangedEvent taskStatusChangedEvent);

    default TaskEventEntity toEntity(TaskEvent taskEvent) {
        return switch (taskEvent) {
            case TaskCreatedEvent createdEvent -> toEntity(createdEvent);
            case TaskDeletedEvent deletedEvent -> toEntity(deletedEvent);
            case TaskReassignedEvent reassignedEvent -> toEntity(reassignedEvent);
            case TaskUpdatedEvent updatedEvent -> toEntity(updatedEvent);
            case TaskTitleChangedEvent titleChangedEvent -> toEntity(titleChangedEvent);
            case TaskDescriptionChangedEvent descriptionChangedEvent -> toEntity(descriptionChangedEvent);
            case TaskStatusChangedEvent taskStatusChangedEvent -> toEntity(taskStatusChangedEvent);
        };
    }

    TaskCreatedEvent toDomain(TaskCreatedEventEntity taskCreatedEventEntity);

    TaskDeletedEvent toDomain(TaskDeletedEventEntity taskDeletedEventEntity);

    TaskReassignedEvent toDomain(TaskReassignedEventEntity taskReassignedEventEntity);

    TaskUpdatedEvent toDomain(TaskUpdatedEventEntity taskUpdatedEventEntity);

    TaskTitleChangedEvent toDomain(TaskTitleChangedEventEntity taskTitleChangedEventEntity);

    TaskDescriptionChangedEvent toDomain(TaskDescriptionChangedEventEntity taskDescriptionChangedEventEntity);

    TaskStatusChangedEvent toDomain(TaskStatusChangedEventEntity taskStatusChangedEventEntity);

    default TaskEvent toDomain(TaskEventEntity taskEventEntity) {
        return switch (taskEventEntity) {
            case TaskCreatedEventEntity createdEventEntity -> toDomain(createdEventEntity);
            case TaskDeletedEventEntity deletedEventEntity -> toDomain(deletedEventEntity);
            case TaskReassignedEventEntity reassignedEventEntity -> toDomain(reassignedEventEntity);
            case TaskUpdatedEventEntity updatedEventEntity -> toDomain(updatedEventEntity);
            case TaskTitleChangedEventEntity titleChangedEventEntity -> toDomain(titleChangedEventEntity);
            case TaskDescriptionChangedEventEntity descriptionChangedEventEntity ->
                toDomain(descriptionChangedEventEntity);
            case TaskStatusChangedEventEntity taskStatusChangedEventEntity -> toDomain(taskStatusChangedEventEntity);
        };
    }
}
