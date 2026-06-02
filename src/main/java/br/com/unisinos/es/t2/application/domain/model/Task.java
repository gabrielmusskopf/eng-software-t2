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
public class Task {
    private String id;
    private String title;
    private String userId;
    private String description;
    private TaskStatus status;
    private boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;

    public Task(String title, String description, String userId) {
        this.title = title;
        this.description = description;
        this.userId = userId;
        this.status = TaskStatus.BACKLOG;
    }
}
