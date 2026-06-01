package br.com.unisinos.es.t2.application.port.in.task;

import br.com.unisinos.es.t2.application.domain.model.TaskUpdatedEvent;

public interface TaskUpdatedNotificationService {

    void notify(TaskUpdatedEvent event);
}
