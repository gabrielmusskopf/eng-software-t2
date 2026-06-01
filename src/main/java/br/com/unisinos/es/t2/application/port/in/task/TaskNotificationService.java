package br.com.unisinos.es.t2.application.port.in.task;

import br.com.unisinos.es.t2.application.domain.model.TaskEvent;

/**
 * Interface for services that handle notifications related to task events.
 * Implementations of this interface will be responsible for sending notifications when specific task events occur
 * @param <E> the type of TaskEvent that this service will handle
 */
public interface TaskNotificationService<E extends TaskEvent> {

    void notify(E event);
}
