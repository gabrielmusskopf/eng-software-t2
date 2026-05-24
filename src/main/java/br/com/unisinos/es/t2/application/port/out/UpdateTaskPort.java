package br.com.unisinos.es.t2.application.port.out;

import br.com.unisinos.es.t2.application.domain.model.Task;

public interface UpdateTaskPort {
    void update(Task task);
}
