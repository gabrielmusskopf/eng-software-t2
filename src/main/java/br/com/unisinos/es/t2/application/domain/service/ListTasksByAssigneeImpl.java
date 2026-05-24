package br.com.unisinos.es.t2.application.domain.service;

import br.com.unisinos.es.t2.application.domain.model.Task;
import br.com.unisinos.es.t2.application.port.in.ListTasksByAssigneeService;
import br.com.unisinos.es.t2.application.port.out.ListTasksByAssigneePort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ListTasksByAssigneeImpl implements ListTasksByAssigneeService {

    private final ListTasksByAssigneePort listTasksByAssigneePort;

    @Override
    public List<Task> list(ListTasksByAssigneeCommand command) {
        return listTasksByAssigneePort.listByAssigneeId(command.assigneeId());
    }
}
