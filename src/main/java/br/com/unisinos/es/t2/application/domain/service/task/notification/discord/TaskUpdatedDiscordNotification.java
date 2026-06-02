package br.com.unisinos.es.t2.application.domain.service.task.notification.discord;

import br.com.unisinos.es.t2.application.domain.model.DiscordWebhookPayload;
import br.com.unisinos.es.t2.application.domain.model.Task;
import br.com.unisinos.es.t2.application.domain.model.TaskReassignedEvent;
import br.com.unisinos.es.t2.application.domain.model.TaskStatusChangedEvent;
import br.com.unisinos.es.t2.application.domain.model.TaskTitleChangedEvent;
import br.com.unisinos.es.t2.application.domain.model.TaskUpdatedEvent;
import br.com.unisinos.es.t2.application.domain.model.User;
import br.com.unisinos.es.t2.application.port.out.discordwebhook.DiscordWebhookProperties;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Getter
@Slf4j
@Component
@ConditionalOnProperty(
        name = {
            "app.notifications.enabled",
            "app.notifications.discord.enabled",
            "app.notifications.discord.events.task-updated.enabled"
        },
        havingValue = "true")
class TaskUpdatedDiscordNotification extends AbstractDiscordNotification<TaskUpdatedEvent> {

    private final String title;
    private final int color;

    TaskUpdatedDiscordNotification(
            DiscordWebhookProperties properties, DiscordNotificationService discordNotificationService) {
        super(properties, discordNotificationService);

        DiscordWebhookProperties.Event taskUpdated = properties.getEvents().getTaskUpdated();
        this.title = Objects.requireNonNullElse(taskUpdated.getTitle(), "Tarefa atualizada");
        this.color = Objects.requireNonNullElse(taskUpdated.getColor(), 0x3498DB);
    }

    @Override
    protected Set<String> getRecipients(TaskUpdatedEvent event) {
        User assignedUser = event.getAssignee();
        User actionUser = event.getTriggeredBy();

        return Stream.of(assignedUser.getId(), actionUser.getId()).collect(Collectors.toSet());
    }

    @Override
    protected List<DiscordWebhookPayload.Field> buildFields(TaskUpdatedEvent event) {
        Task task = event.getTask();
        User actionUser = event.getTriggeredBy();

        DiscordWebhookPayload.EmbedFieldsBuilder fieldsBuilder = new DiscordWebhookPayload.EmbedFieldsBuilder();
        this.addTitleFields(event, fieldsBuilder);
        fieldsBuilder.addField("Descrição", task.getDescription());
        fieldsBuilder.addField("Task ID", task.getId());
        this.addStatusFields(event, fieldsBuilder);
        this.addAssigneeFields(event, fieldsBuilder);
        fieldsBuilder.addField("Atualizada por", actionUser.getNameWithEmail());

        return fieldsBuilder.build();
    }

    private void addTitleFields(TaskUpdatedEvent event, DiscordWebhookPayload.EmbedFieldsBuilder fieldsBuilder) {
        Task task = event.getTask();
        if (event.hasEventType(TaskTitleChangedEvent.class)) {
            for (TaskTitleChangedEvent titleChangedEvent : event.getEventsByType(TaskTitleChangedEvent.class)) {
                fieldsBuilder
                        .addField("Título anterior", titleChangedEvent.getTitleBefore())
                        .addField("Título novo", task.getTitle());
            }
        } else {
            fieldsBuilder.addField("Título", task.getTitle());
        }
    }

    private void addAssigneeFields(TaskUpdatedEvent event, DiscordWebhookPayload.EmbedFieldsBuilder fieldsBuilder) {
        if (event.hasEventType(TaskReassignedEvent.class)) {
            for (TaskReassignedEvent reassignedEvent : event.getEventsByType(TaskReassignedEvent.class)) {
                fieldsBuilder
                        .addField("De", reassignedEvent.getAssigneeBefore().getNameWithEmail())
                        .addField("Para", reassignedEvent.getAssigneeAfter().getNameWithEmail());
            }
        } else {
            fieldsBuilder.addField("Vinculado a", event.getAssignee().getNameWithEmail());
        }
    }

    private void addStatusFields(TaskUpdatedEvent event, DiscordWebhookPayload.EmbedFieldsBuilder fieldsBuilder) {
        if (event.hasEventType(TaskStatusChangedEvent.class)) {
            for (TaskStatusChangedEvent statusChangedEvent : event.getEventsByType(TaskStatusChangedEvent.class)) {
                fieldsBuilder.addField(
                        "Status",
                        "De `" + statusChangedEvent.getStatusBefore().getDisplayName() + "` para `"
                                + statusChangedEvent.getTask().getStatus().getDisplayName() + "`");
            }
        } else {
            fieldsBuilder.addField("Status", event.getTask().getStatus().getDisplayName());
        }
    }
}
