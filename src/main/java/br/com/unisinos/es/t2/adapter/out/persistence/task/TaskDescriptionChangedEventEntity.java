package br.com.unisinos.es.t2.adapter.out.persistence.task;

import br.com.unisinos.es.t2.application.domain.model.TaskEventType;
import br.com.unisinos.es.t2.application.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TypeAlias("TASK_DESCRIPTION_CHANGED")
final class TaskDescriptionChangedEventEntity extends TaskEventEntity {

    private TaskEventType eventType;
    private User assignee;
    private String descriptionBefore;
}
