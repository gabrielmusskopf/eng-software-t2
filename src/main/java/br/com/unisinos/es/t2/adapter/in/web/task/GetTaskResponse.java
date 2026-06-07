package br.com.unisinos.es.t2.adapter.in.web.task;

import lombok.Data;

@Data
class GetTaskResponse {
    private String id;
    private String title;
    private String description;
    private String assigneeId;
}
