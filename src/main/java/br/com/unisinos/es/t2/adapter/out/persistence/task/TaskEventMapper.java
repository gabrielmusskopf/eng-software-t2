package br.com.unisinos.es.t2.adapter.out.persistence.task;

import br.com.unisinos.es.t2.application.domain.model.TaskCreatedEvent;
import br.com.unisinos.es.t2.application.domain.model.TaskEvent;
import org.mapstruct.Mapper;
import org.mapstruct.SubclassExhaustiveStrategy;
import org.mapstruct.SubclassMapping;

@Mapper(componentModel = "spring", subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION)
public interface TaskEventMapper {

    @SubclassMapping(source = TaskCreatedEvent.class, target = TaskCreatedEventEntity.class)
    TaskEventEntity toEntity(TaskEvent taskEvent);

    @SubclassMapping(source = TaskCreatedEventEntity.class, target = TaskCreatedEvent.class)
    TaskEvent toDomain(TaskEventEntity taskEventEntity);
}
