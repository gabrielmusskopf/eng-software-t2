package br.com.unisinos.es.t2.adapter.in.web.task;

import br.com.unisinos.es.t2.adapter.in.web.ApiResponse;
import br.com.unisinos.es.t2.application.domain.model.Task;
import br.com.unisinos.es.t2.application.port.in.task.CreateTaskService;
import br.com.unisinos.es.t2.application.port.in.task.CreateTaskService.CreateTaskCommand;
import br.com.unisinos.es.t2.application.port.in.task.DeleteTaskService;
import br.com.unisinos.es.t2.application.port.in.task.DeleteTaskService.DeleteTaskCommand;
import br.com.unisinos.es.t2.application.port.in.task.UpdateTaskService;
import br.com.unisinos.es.t2.application.port.in.task.UpdateTaskService.UpdateTaskCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
class TaskController {

    private final TaskMapper taskMapper;
    private final CreateTaskService createTaskService;
    private final DeleteTaskService deleteTaskService;
    private final UpdateTaskService updateTaskService;

    @PostMapping
    public ResponseEntity<ApiResponse<CreateTaskResponse>> createTask(@Valid @RequestBody CreateTaskRequest request) {
        CreateTaskCommand command = taskMapper.toCreateTaskCommand(request);
        Task task = createTaskService.createTask(command);
        CreateTaskResponse createTaskResponse = taskMapper.toCreateTaskResponse(task);
        return ApiResponse.success(201, "Task created successfully", createTaskResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable String id) {
        deleteTaskService.deleteTask(new DeleteTaskCommand(id));
        return ApiResponse.success(200, "Task deleted successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UpdateTaskResponse>> updateTask(
            @PathVariable String id, @Valid @RequestBody UpdateTaskRequest request) {
        UpdateTaskCommand command = taskMapper.toUpdateTaskCommand(id, request);
        Task updatedTask = updateTaskService.updateTask(command);
        UpdateTaskResponse response = taskMapper.toUpdateTaskResponse(updatedTask);
        return ApiResponse.success(200, "Task updated successfully", response);
    }
}
