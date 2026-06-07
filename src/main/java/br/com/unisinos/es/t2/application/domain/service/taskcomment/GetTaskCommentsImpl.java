package br.com.unisinos.es.t2.application.domain.service.taskcomment;

import br.com.unisinos.es.t2.application.domain.exception.NotFoundException;
import br.com.unisinos.es.t2.application.domain.model.TaskComment;
import br.com.unisinos.es.t2.application.port.in.taskcomment.GetTaskCommentsService;
import br.com.unisinos.es.t2.application.port.out.task.GetTaskByIdPort;
import br.com.unisinos.es.t2.application.port.out.taskcomment.GetTaskCommentsByTaskIdPort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class GetTaskCommentsImpl implements GetTaskCommentsService {

    private final GetTaskByIdPort getTaskByIdPort;
    private final GetTaskCommentsByTaskIdPort getTaskCommentsByTaskIdPort;

    @Override
    public List<TaskComment> getComments(GetTaskCommentsCommand command) {
        log.debug("Getting comments for task id {}", command.taskId());
        getTaskByIdPort.getById(command.taskId()).orElseThrow(() -> new NotFoundException("Task not found"));
        return getTaskCommentsByTaskIdPort.getByTaskId(command.taskId());
    }
}
