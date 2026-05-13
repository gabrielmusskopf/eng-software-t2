package br.com.unisinos.es.t2.adapter.in.web;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @NotEmpty
    private String name;

    @NotEmpty
    private String password;

    @NotEmpty
    private String email;
}
