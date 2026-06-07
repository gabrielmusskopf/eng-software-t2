package br.com.unisinos.es.t2.application.domain.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskComment {
    private String id;
    private String taskId;
    private String userId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;

    public TaskComment(String taskId, String userId, String content) {
        this.taskId = taskId;
        this.userId = userId;
        this.content = content;
    }
}
