package br.com.unisinos.es.t2.application.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class TaskDeletedEvent extends TaskEvent {

    private final TaskEventType eventType = TaskEventType.DELETED;
    private User assignee;

    public TaskDeletedEvent(Task task, User triggeredBy, User assignee) {
        this.task = task;
        this.triggeredBy = triggeredBy;
        this.assignee = assignee;
    }
}
