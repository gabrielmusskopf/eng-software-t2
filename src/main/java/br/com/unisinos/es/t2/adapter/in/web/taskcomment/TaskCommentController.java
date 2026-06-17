package br.com.unisinos.es.t2.adapter.in.web.taskcomment;

import br.com.unisinos.es.t2.adapter.in.web.ApiResponse;
import br.com.unisinos.es.t2.application.domain.model.TaskComment;
import br.com.unisinos.es.t2.application.port.in.taskcomment.CreateTaskCommentService;
import br.com.unisinos.es.t2.application.port.in.taskcomment.CreateTaskCommentService.CreateTaskCommentCommand;
import br.com.unisinos.es.t2.application.port.in.taskcomment.DeleteTaskCommentService;
import br.com.unisinos.es.t2.application.port.in.taskcomment.DeleteTaskCommentService.DeleteTaskCommentCommand;
import br.com.unisinos.es.t2.application.port.in.taskcomment.GetTaskCommentsService;
import br.com.unisinos.es.t2.application.port.in.taskcomment.GetTaskCommentsService.GetTaskCommentsCommand;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Comments", description = "Comentários das tarefas")
@RestController
@RequestMapping("/tasks/{taskId}/comments")
@RequiredArgsConstructor
class TaskCommentController {

    private final TaskCommentMapper taskCommentMapper;
    private final CreateTaskCommentService createTaskCommentService;
    private final GetTaskCommentsService getTaskCommentsService;
    private final DeleteTaskCommentService deleteTaskCommentService;

    @PostMapping
    public ResponseEntity<ApiResponse<TaskCommentResponse>> createComment(
            @PathVariable String taskId, @Valid @RequestBody CreateTaskCommentRequest request) {
        CreateTaskCommentCommand command = new CreateTaskCommentCommand(taskId, request.getContent());
        TaskComment comment = createTaskCommentService.createComment(command);
        TaskCommentResponse response = taskCommentMapper.toResponse(comment);
        return ApiResponse.success(201, "Comment created successfully", response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TaskCommentResponse>>> getComments(@PathVariable String taskId) {
        List<TaskComment> comments = getTaskCommentsService.getComments(new GetTaskCommentsCommand(taskId));
        List<TaskCommentResponse> response = taskCommentMapper.toResponseList(comments);
        return ApiResponse.success(200, "Comments retrieved successfully", response);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable String taskId, @PathVariable String commentId) {
        deleteTaskCommentService.deleteComment(new DeleteTaskCommentCommand(taskId, commentId));
        return ApiResponse.success(200, "Comment deleted successfully");
    }
}
