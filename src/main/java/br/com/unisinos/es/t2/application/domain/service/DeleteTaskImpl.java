package br.com.unisinos.es.t2.application.domain.service;

import br.com.unisinos.es.t2.application.domain.exception.NotFoundException;
import br.com.unisinos.es.t2.application.port.in.DeleteTaskService;
import br.com.unisinos.es.t2.application.port.out.DeleteTaskPort;
import br.com.unisinos.es.t2.application.port.out.ExistsTaskByIdPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class DeleteTaskImpl implements DeleteTaskService {

    private final ExistsTaskByIdPort existsTaskByIdPort;
    private final DeleteTaskPort deleteTaskPort;

    @Override
    public void delete(DeleteTaskCommand command) {
        if (!existsTaskByIdPort.exists(command.id())) {
            throw new NotFoundException("Task not found");
        }
        deleteTaskPort.delete(command.id());
    }
}
