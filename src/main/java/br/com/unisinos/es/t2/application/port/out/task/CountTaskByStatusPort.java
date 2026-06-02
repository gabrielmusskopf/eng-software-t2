package br.com.unisinos.es.t2.application.port.out.task;

import br.com.unisinos.es.t2.application.domain.model.TaskStatus;

public interface CountTaskByStatusPort {

    long countByStatus(TaskStatus status);
}
