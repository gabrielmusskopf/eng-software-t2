package br.com.unisinos.es.t2.adapter.in.web.task;

import br.com.unisinos.es.t2.application.domain.model.TaskStatus;
import lombok.Data;

@Data
class UpdateTaskResponse {
    private String id;
    private String title;
    private String description;
    private String assigneeId;
    private TaskStatus status;
}
