package br.com.unisinos.es.t2.adapter.in.web.auth;

import br.com.unisinos.es.t2.adapter.in.web.ApiResponse;
import br.com.unisinos.es.t2.application.port.in.auth.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
class AuthController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest request) {
        LoginService.LoginCommand command = new LoginService.LoginCommand(request.getEmail(), request.getPassword());
        String token = loginService.generateToken(command);
        LoginResponse response = new LoginResponse(token);
        return ApiResponse.success(200, "Login successful", response);
    }
}
