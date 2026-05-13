package br.com.unisinos.es.t2.adapter.in.web;

import br.com.unisinos.es.t2.application.domain.model.User;
import br.com.unisinos.es.t2.application.port.in.CreateUserService;
import br.com.unisinos.es.t2.application.port.in.CreateUserService.CreateUserCommand;
import br.com.unisinos.es.t2.application.port.in.DeleteUserService;
import br.com.unisinos.es.t2.application.port.in.DeleteUserService.DeleteUserCommand;
import br.com.unisinos.es.t2.application.port.in.GetUserService;
import br.com.unisinos.es.t2.application.port.in.GetUserService.GetUserCommand;
import br.com.unisinos.es.t2.application.port.in.UpdateUserService;
import br.com.unisinos.es.t2.application.port.in.UpdateUserService.UpdateUserCommand;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
class UserController {

    private final UserMapper userMapper;
    private final CreateUserService createUserService;
    private final GetUserService getUserService;
    private final UpdateUserService updateUserService;
    private final DeleteUserService deleteUserService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<CreateUserResponse>> createUser(@Valid @RequestBody CreateUserRequest request) {
        CreateUserCommand command = new CreateUserCommand(request.getName(), request.getEmail(), request.getPassword());
        User user = createUserService.create(command);
        CreateUserResponse userResponse = userMapper.toCreateUserResponse(user);
        return ApiResponse.success(201, "User created successfully", userResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GetUserResponse>> getUser(@PathVariable String id) {
        User user = getUserService.get(new GetUserCommand(id));
        GetUserResponse userResponse = userMapper.toUserResponse(user);
        return ApiResponse.success(200, "User retrieved successfully", userResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateUser(@PathVariable String id, @Valid UpdateUserRequest request) {
        updateUserService.update(new UpdateUserCommand(id, request.getName(), request.getEmail()));
        return ApiResponse.success(200, "User updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String id) {
        deleteUserService.delete(new DeleteUserCommand(id));
        return ApiResponse.success(200, "User deleted successfully");
    }
}
