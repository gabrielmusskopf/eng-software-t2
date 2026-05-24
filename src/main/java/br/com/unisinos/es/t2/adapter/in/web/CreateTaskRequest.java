package br.com.unisinos.es.t2.adapter.in.web;

import br.com.unisinos.es.t2.application.domain.model.TaskStatus;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTaskRequest {
    @NotEmpty
    private String title;

    private String description;

    @NotEmpty
    private String creatorId;

    private String assigneeId;

    private TaskStatus status;
}
