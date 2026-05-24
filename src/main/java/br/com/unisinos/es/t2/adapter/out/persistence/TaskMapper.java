package br.com.unisinos.es.t2.adapter.out.persistence;

import br.com.unisinos.es.t2.application.domain.model.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface TaskMapper {

    TaskEntity toEntity(Task task);

    Task toDomain(TaskEntity entity);
}
