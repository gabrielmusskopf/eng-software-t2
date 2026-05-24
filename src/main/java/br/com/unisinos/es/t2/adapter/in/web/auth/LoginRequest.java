package br.com.unisinos.es.t2.adapter.in.web.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
class LoginRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
