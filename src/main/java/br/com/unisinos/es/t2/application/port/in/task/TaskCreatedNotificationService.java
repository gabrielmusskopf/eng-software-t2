package br.com.unisinos.es.t2.application.port.in.task;

import br.com.unisinos.es.t2.application.domain.model.TaskCreatedEvent;

public interface TaskCreatedNotificationService {

    void notify(TaskCreatedEvent event);
}
