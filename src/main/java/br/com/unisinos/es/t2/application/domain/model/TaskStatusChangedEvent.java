package br.com.unisinos.es.t2.application.domain.model;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public final class TaskStatusChangedEvent extends TaskEvent {

    private final TaskEventType eventType = TaskEventType.STATUS_CHANGED;
    private TaskStatus statusBefore;
    private LocalDateTime statusLastChangedAt;

    public TaskStatusChangedEvent(Task task, User triggeredBy) {
        this.task = task;
        this.triggeredBy = triggeredBy;
        this.statusBefore = task.getStatus();
        this.statusLastChangedAt = task.getStatusUpdatedAt();
    }
}
