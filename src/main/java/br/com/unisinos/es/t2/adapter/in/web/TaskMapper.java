package br.com.unisinos.es.t2.adapter.in.web;

import br.com.unisinos.es.t2.application.domain.model.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface TaskMapper {

    CreateTaskResponse toCreateTaskResponse(Task task);

    GetTaskResponse toTaskResponse(Task task);
}
