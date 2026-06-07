package br.com.unisinos.es.t2.application.domain.service.task;

import br.com.unisinos.es.t2.application.domain.exception.NotFoundException;
import br.com.unisinos.es.t2.application.domain.model.Task;
import br.com.unisinos.es.t2.application.port.in.task.GetTasksByUserService;
import br.com.unisinos.es.t2.application.port.out.task.GetTasksByUserIdPort;
import br.com.unisinos.es.t2.application.port.out.user.ExistsUserByIdPort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class GetTasksByUserImpl implements GetTasksByUserService {

    private final ExistsUserByIdPort existsUserByIdPort;
    private final GetTasksByUserIdPort getTasksByUserIdPort;

    @Override
    public List<Task> getByAssignedUser(GetTasksByUserCommand command) {
        log.debug("Getting tasks assigned to user id {}", command.userId());
        if (!existsUserByIdPort.exists(command.userId())) {
            throw new NotFoundException("User not found");
        }
        return getTasksByUserIdPort.getByUserId(command.userId());
    }
}
