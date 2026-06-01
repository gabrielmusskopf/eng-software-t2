package br.com.unisinos.es.t2.application.domain.model;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public final class TaskUpdatedEvent extends TaskEvent {

    private final TaskEventType eventType = TaskEventType.UPDATED;
    private final User assignee;
    private final List<TaskEvent> updateEvents;

    public TaskUpdatedEvent(Task task, User triggeredBy, User assignee, List<TaskEvent> updateEvents) {
        this.task = task;
        this.triggeredBy = triggeredBy;
        this.assignee = assignee;
        this.updateEvents = updateEvents;
    }

    public boolean hasEventType(Class<? extends TaskEvent> eventClass) {
        return updateEvents.stream().anyMatch(event -> event.getClass().equals(eventClass));
    }

    public <T extends TaskEvent> List<T> getEventsByType(Class<T> eventClass) {
        return updateEvents.stream()
                .filter(event -> event.getClass().equals(eventClass))
                .map(eventClass::cast)
                .toList();
    }
}
