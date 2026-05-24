package br.com.unisinos.es.t2.application.port.out.task;

import br.com.unisinos.es.t2.application.domain.model.Task;

public interface CreateTaskPort {

    Task createTask(Task task);
}
