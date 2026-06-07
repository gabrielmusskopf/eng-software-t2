package br.com.unisinos.es.t2.adapter.in.web.taskcomment;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
class CreateTaskCommentRequest {
    @NotBlank
    private String content;
}
