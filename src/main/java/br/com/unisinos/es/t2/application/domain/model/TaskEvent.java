package br.com.unisinos.es.t2.application.domain.model;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public abstract sealed class TaskEvent implements Event
        permits TaskCreatedEvent,
                TaskDeletedEvent,
                TaskReassignedEvent,
                TaskTitleChangedEvent,
                TaskDescriptionChangedEvent,
                TaskUpdatedEvent {

    protected String id;
    protected Task task;
    protected User triggeredBy;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
    protected boolean deleted;
    protected Long version;

    public abstract TaskEventType getEventType();
}
