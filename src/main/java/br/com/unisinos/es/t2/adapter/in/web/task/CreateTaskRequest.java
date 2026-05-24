package br.com.unisinos.es.t2.adapter.in.web.task;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateTaskRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    private String userId; // TODO: Obter do token de autenticação
}
