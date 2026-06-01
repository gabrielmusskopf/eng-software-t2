package br.com.unisinos.es.t2.application.port.in.task;

import br.com.unisinos.es.t2.application.domain.model.TaskDeletedEvent;

public interface TaskDeletedNotificationService {

    void notify(TaskDeletedEvent event);
}
