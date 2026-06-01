package br.com.unisinos.es.t2.adapter.out.persistence.task;

import br.com.unisinos.es.t2.adapter.out.persistence.Entity;
import br.com.unisinos.es.t2.application.domain.model.Task;
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
    @JsonSubTypes.Type(value = TaskCreatedEventEntity.class, name = "TASK_CREATED"),
    @JsonSubTypes.Type(value = TaskDeletedEventEntity.class, name = "TASK_DELETED"),
    @JsonSubTypes.Type(value = TaskReassignedEventEntity.class, name = "TASK_REASSIGNED"),
    @JsonSubTypes.Type(value = TaskUpdatedEventEntity.class, name = "TASK_UPDATED"),
    @JsonSubTypes.Type(value = TaskTitleChangedEventEntity.class, name = "TASK_TITLE_CHANGED"),
    @JsonSubTypes.Type(value = TaskDescriptionChangedEventEntity.class, name = "TASK_DESCRIPTION_CHANGED")
})
@Document(collection = "task_events")
public abstract sealed class TaskEventEntity extends Entity
        permits TaskCreatedEventEntity,
                TaskDeletedEventEntity,
                TaskDescriptionChangedEventEntity,
                TaskReassignedEventEntity,
                TaskTitleChangedEventEntity,
                TaskUpdatedEventEntity {
    @NotBlank
    protected Task task;

    @NotBlank
    protected User triggeredBy;

    protected abstract TaskEventType getEventType();
}
