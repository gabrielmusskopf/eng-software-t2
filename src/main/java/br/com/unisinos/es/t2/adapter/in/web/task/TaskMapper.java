package br.com.unisinos.es.t2.adapter.in.web.task;

import br.com.unisinos.es.t2.application.domain.model.Task;
import br.com.unisinos.es.t2.application.port.in.task.CreateTaskService;
import br.com.unisinos.es.t2.application.port.in.task.UpdateTaskService;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
interface TaskMapper {

    CreateTaskService.CreateTaskCommand toCreateTaskCommand(CreateTaskRequest request);

    CreateTaskResponse toCreateTaskResponse(Task task);

    UpdateTaskService.UpdateTaskCommand toUpdateTaskCommand(String taskId, UpdateTaskRequest request);

    @Mapping(source = "userId", target = "assigneeId")
    UpdateTaskResponse toUpdateTaskResponse(Task updatedTask);

    @Mapping(source = "userId", target = "assigneeId")
    GetTaskResponse toGetTaskResponse(Task task);

    List<GetTaskResponse> toGetTaskResponseList(List<Task> tasks);
}
