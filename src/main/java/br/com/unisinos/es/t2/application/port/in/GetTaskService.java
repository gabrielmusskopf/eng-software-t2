package br.com.unisinos.es.t2.application.port.in;

import br.com.unisinos.es.t2.application.domain.model.Task;

public interface GetTaskService {

    Task get(GetTaskCommand command);

    record GetTaskCommand(String id) {}
}
