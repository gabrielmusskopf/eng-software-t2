package br.com.unisinos.es.t2.application.domain.model;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public abstract class TaskEvent implements Event {

    protected String id;
    protected String taskId;
    protected String userId;
    protected LocalDateTime createdAt;
    protected Task task;

    public abstract TaskEventType getEventType();
}
