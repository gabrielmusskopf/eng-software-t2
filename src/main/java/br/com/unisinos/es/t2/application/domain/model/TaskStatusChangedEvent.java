package br.com.unisinos.es.t2.application.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public final class TaskStatusChangedEvent extends TaskEvent {

    private final TaskEventType eventType = TaskEventType.STATUS_CHANGED;
    private TaskStatus statusBefore;

    public TaskStatusChangedEvent(Task task, User triggeredBy, TaskStatus statusBefore) {
        this.task = task;
        this.triggeredBy = triggeredBy;
        this.statusBefore = statusBefore;
    }
}
