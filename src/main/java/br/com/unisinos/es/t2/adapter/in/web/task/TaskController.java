package br.com.unisinos.es.t2.adapter.in.web.task;

import br.com.unisinos.es.t2.adapter.in.web.ApiResponse;
import br.com.unisinos.es.t2.application.domain.model.Task;
import br.com.unisinos.es.t2.application.port.in.task.CreateTaskService;
import br.com.unisinos.es.t2.application.port.in.task.CreateTaskService.CreateTaskCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
class TaskController {

    private final TaskMapper taskMapper;
    private final CreateTaskService createTaskService;

    @PostMapping
    public ResponseEntity<ApiResponse<CreateTaskResponse>> createTask(@Valid @RequestBody CreateTaskRequest request) {
        CreateTaskCommand command = taskMapper.toCreateTaskCommand(request);
        Task task = createTaskService.createTask(command);
        CreateTaskResponse createTaskResponse = taskMapper.toCreateTaskResponse(task);
        return ApiResponse.success(201, "Task created successfully", createTaskResponse);
    }
}
