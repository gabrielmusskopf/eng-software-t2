package br.com.unisinos.es.t2.adapter.out.persistence.task;

import br.com.unisinos.es.t2.application.domain.model.TaskEventType;
import br.com.unisinos.es.t2.application.domain.model.TaskStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TypeAlias("TASK_STATUS_CHANGED")
final class TaskStatusChangedEventEntity extends TaskEventEntity {

    private TaskEventType eventType;
    private TaskStatus statusBefore;
    private LocalDateTime statusLastChangedAt;
}
