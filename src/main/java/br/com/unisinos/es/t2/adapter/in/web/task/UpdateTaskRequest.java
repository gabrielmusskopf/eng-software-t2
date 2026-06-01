package br.com.unisinos.es.t2.adapter.in.web.task;

import lombok.Data;

@Data
class UpdateTaskRequest {
    private String title;
    private String description;
    private String assigneeId;
}
