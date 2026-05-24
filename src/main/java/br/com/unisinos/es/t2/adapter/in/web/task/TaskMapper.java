package br.com.unisinos.es.t2.adapter.in.web.task;

import br.com.unisinos.es.t2.application.domain.model.Task;
import br.com.unisinos.es.t2.application.port.in.task.CreateTaskService;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface TaskMapper {

    CreateTaskService.CreateTaskCommand toCreateTaskCommand(CreateTaskRequest request);

    CreateTaskResponse toCreateTaskResponse(Task task);
}
