package br.com.unisinos.es.t2.application.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public final class TaskTitleChangedEvent extends TaskEvent {

    private final TaskEventType eventType = TaskEventType.TITLE_CHANGED;
    private User assignee;
    private String titleBefore;

    public TaskTitleChangedEvent(Task task, User triggeredBy, User assignee, String titleBefore) {
        this.task = task;
        this.triggeredBy = triggeredBy;
        this.assignee = assignee;
        this.titleBefore = titleBefore;
    }
}
