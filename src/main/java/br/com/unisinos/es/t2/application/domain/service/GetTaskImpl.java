package br.com.unisinos.es.t2.application.domain.service;

import br.com.unisinos.es.t2.application.domain.exception.NotFoundException;
import br.com.unisinos.es.t2.application.domain.model.Task;
import br.com.unisinos.es.t2.application.port.in.GetTaskService;
import br.com.unisinos.es.t2.application.port.out.GetTaskByIdPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class GetTaskImpl implements GetTaskService {

    private final GetTaskByIdPort getTaskByIdPort;

    @Override
    public Task get(GetTaskCommand command) {
        return getTaskByIdPort.getById(command.id()).orElseThrow(() -> new NotFoundException("Task not found"));
    }
}
