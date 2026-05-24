package br.com.unisinos.es.t2.application.port.out.task;

import br.com.unisinos.es.t2.application.domain.model.TaskEvent;

public interface CreateTaskEventPort {

    TaskEvent createTaskEvent(TaskEvent event);
}
