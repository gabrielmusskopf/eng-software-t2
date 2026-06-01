package br.com.unisinos.es.t2.application.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public final class TaskDescriptionChangedEvent extends TaskEvent {

    private final TaskEventType eventType = TaskEventType.DESCRIPTION_CHANGED;
    private User assignee;
    private String descriptionBefore;

    public TaskDescriptionChangedEvent(Task task, User triggeredBy, User assignee, String descriptionBefore) {
        this.task = task;
        this.triggeredBy = triggeredBy;
        this.assignee = assignee;
        this.descriptionBefore = descriptionBefore;
    }
}
