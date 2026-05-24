package br.com.unisinos.es.t2.application.domain.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Task {
    private String id;
    private String title;
    private String description;
    private TaskStatus status;
    private String creatorId;
    private String assigneeId;
    private boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
