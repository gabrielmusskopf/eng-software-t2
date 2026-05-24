package br.com.unisinos.es.t2.adapter.out.persistence.task;

import br.com.unisinos.es.t2.adapter.out.persistence.Entity;
import br.com.unisinos.es.t2.application.domain.model.EventType;
import br.com.unisinos.es.t2.application.domain.model.TaskCreatedEvent;
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
})
@Document(collection = "task_events")
public abstract class TaskEventEntity extends Entity {
    @NotBlank
    protected String taskId;

    @NotBlank
    protected String userId;

    protected abstract EventType getEventType();
}
