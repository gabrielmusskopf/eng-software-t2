package br.com.unisinos.es.t2.adapter.out.persistence.task;

import br.com.unisinos.es.t2.adapter.out.persistence.Entity;
import br.com.unisinos.es.t2.application.domain.model.Task;
import br.com.unisinos.es.t2.application.domain.model.TaskCreatedEvent;
import br.com.unisinos.es.t2.application.domain.model.TaskDeletedEvent;
import br.com.unisinos.es.t2.application.domain.model.TaskEventType;
import br.com.unisinos.es.t2.application.domain.model.User;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "eventType",
        visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = TaskCreatedEvent.class, name = "TASK_CREATED"),
    @JsonSubTypes.Type(value = TaskDeletedEvent.class, name = "TASK_DELETED"),
})
@Document(collection = "task_events")
public abstract sealed class TaskEventEntity extends Entity permits TaskCreatedEventEntity, TaskDeletedEventEntity {
    @NotBlank
    protected Task task;

    @NotBlank
    protected User triggeredBy;

    protected abstract TaskEventType getEventType();
}
