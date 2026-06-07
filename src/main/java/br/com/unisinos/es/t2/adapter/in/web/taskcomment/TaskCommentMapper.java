package br.com.unisinos.es.t2.adapter.in.web.taskcomment;

import br.com.unisinos.es.t2.application.domain.model.TaskComment;
import br.com.unisinos.es.t2.application.port.in.taskcomment.CreateTaskCommentService;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface TaskCommentMapper {

    CreateTaskCommentService.CreateTaskCommentCommand toCreateCommand(String taskId, CreateTaskCommentRequest request);

    TaskCommentResponse toResponse(TaskComment comment);

    List<TaskCommentResponse> toResponseList(List<TaskComment> comments);
}
