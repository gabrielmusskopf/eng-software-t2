package br.com.unisinos.es.t2.adapter.in.web.taskcomment;

import java.time.LocalDateTime;
import lombok.Data;

@Data
class TaskCommentResponse {
    private String id;
    private String taskId;
    private String userId;
    private String content;
    private LocalDateTime createdAt;
}
