package br.com.unisinos.es.t2.application.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TaskCreatedEvent extends TaskEvent {

    private final TaskEventType eventType = TaskEventType.CREATED;

    public TaskCreatedEvent(Task task) {
        this.task = task;
        this.taskId = task.getId();
        this.userId = task.getUserId();
    }
}
