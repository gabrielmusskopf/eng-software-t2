package br.com.unisinos.es.t2.adapter.in.web;

import br.com.unisinos.es.t2.application.domain.model.Task;
import br.com.unisinos.es.t2.application.port.in.CreateTaskService;
import br.com.unisinos.es.t2.application.port.in.CreateTaskService.CreateTaskCommand;
import br.com.unisinos.es.t2.application.port.in.DeleteTaskService;
import br.com.unisinos.es.t2.application.port.in.DeleteTaskService.DeleteTaskCommand;
import br.com.unisinos.es.t2.application.port.in.GetTaskService;
import br.com.unisinos.es.t2.application.port.in.GetTaskService.GetTaskCommand;
import br.com.unisinos.es.t2.application.port.in.ListTasksByAssigneeService;
import br.com.unisinos.es.t2.application.port.in.ListTasksByAssigneeService.ListTasksByAssigneeCommand;
import br.com.unisinos.es.t2.application.port.in.UpdateTaskService;
import br.com.unisinos.es.t2.application.port.in.UpdateTaskService.UpdateTaskCommand;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
class TaskController {

    private final TaskMapper taskMapper;
    private final CreateTaskService createTaskService;
    private final GetTaskService getTaskService;
    private final UpdateTaskService updateTaskService;
    private final DeleteTaskService deleteTaskService;
    private final ListTasksByAssigneeService listTasksByAssigneeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<CreateTaskResponse>> createTask(@Valid @RequestBody CreateTaskRequest request) {
        CreateTaskCommand command = new CreateTaskCommand(
                request.getTitle(), request.getDescription(), request.getCreatorId(), request.getAssigneeId());
        Task task = createTaskService.create(command);
        CreateTaskResponse response = taskMapper.toCreateTaskResponse(task);
        return ApiResponse.success(201, "Task created successfully", response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GetTaskResponse>> getTask(@PathVariable String id) {
        Task task = getTaskService.get(new GetTaskCommand(id));
        GetTaskResponse response = taskMapper.toTaskResponse(task);
        return ApiResponse.success(200, "Task retrieved successfully", response);
    }

    @GetMapping(params = "assignedTo")
    public ResponseEntity<ApiResponse<List<GetTaskResponse>>> listByAssignee(
            @RequestParam("assignedTo") String assignedTo) {
        List<GetTaskResponse> responses =
                listTasksByAssigneeService.list(new ListTasksByAssigneeCommand(assignedTo)).stream()
                        .map(taskMapper::toTaskResponse)
                        .toList();
        return ApiResponse.success(200, "Tasks retrieved successfully", responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateTask(
            @PathVariable String id, @Valid @RequestBody UpdateTaskRequest request) {
        updateTaskService.update(new UpdateTaskCommand(
                id, request.getTitle(), request.getDescription(), request.getStatus(), request.getAssigneeId()));
        return ApiResponse.success(200, "Task updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable String id) {
        deleteTaskService.delete(new DeleteTaskCommand(id));
        return ApiResponse.success(200, "Task deleted successfully");
    }
}
