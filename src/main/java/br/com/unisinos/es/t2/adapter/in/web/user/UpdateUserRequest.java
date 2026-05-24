package br.com.unisinos.es.t2.adapter.in.web.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @NotEmpty
    private String name;

    @NotEmpty
    private String email;
}
