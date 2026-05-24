package br.com.unisinos.es.t2.application.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TaskCreatedEvent extends TaskEvent {

    private final EventType eventType = EventType.TASK_CREATED;

    public TaskCreatedEvent(Task task) {
        this.taskId = task.getId();
        this.userId = task.getUserId();
    }
}
