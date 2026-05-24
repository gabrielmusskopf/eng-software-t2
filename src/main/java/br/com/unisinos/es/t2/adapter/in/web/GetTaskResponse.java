package br.com.unisinos.es.t2.adapter.in.web;

import br.com.unisinos.es.t2.application.domain.model.TaskStatus;
import lombok.Data;

@Data
public class GetTaskResponse {
    private String id;
    private String title;
    private String description;
    private TaskStatus status;
    private String creatorId;
    private String assigneeId;
}
