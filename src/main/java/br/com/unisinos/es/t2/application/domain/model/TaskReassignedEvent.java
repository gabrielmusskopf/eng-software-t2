package br.com.unisinos.es.t2.application.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public final class TaskReassignedEvent extends TaskEvent {

    private final TaskEventType eventType = TaskEventType.REASSIGNED;
    private User assigneeBefore;
    private User assigneeAfter;

    public TaskReassignedEvent(Task task, User triggeredBy, User assigneeBefore, User assigneeAfter) {
        this.task = task;
        this.triggeredBy = triggeredBy;
        this.assigneeBefore = assigneeBefore;
        this.assigneeAfter = assigneeAfter;
    }
}
