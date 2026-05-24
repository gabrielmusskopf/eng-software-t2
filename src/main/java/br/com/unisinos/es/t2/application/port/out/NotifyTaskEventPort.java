package br.com.unisinos.es.t2.application.port.out;

import br.com.unisinos.es.t2.application.domain.model.Task;
import br.com.unisinos.es.t2.application.domain.model.TaskEvent;

public interface NotifyTaskEventPort {

    void notify(TaskEvent event, Task task);
}
