package br.com.unisinos.es.t2.application.domain.service.task.notification.discord;

import br.com.unisinos.es.t2.application.domain.model.DiscordWebhookPayload;
import br.com.unisinos.es.t2.application.domain.model.Task;
import br.com.unisinos.es.t2.application.domain.model.TaskCreatedEvent;
import br.com.unisinos.es.t2.application.domain.model.User;
import br.com.unisinos.es.t2.application.port.out.discordwebhook.DiscordWebhookProperties;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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
            "app.notifications.discord.events.task-created.enabled"
        },
        havingValue = "true")
class TaskCreatedDiscordNotification extends AbstractDiscordNotification<TaskCreatedEvent> {

    private final String title;
    private final int color;

    public TaskCreatedDiscordNotification(
            DiscordWebhookProperties properties, DiscordNotificationService discordNotificationService) {
        super(properties, discordNotificationService);

        DiscordWebhookProperties.Event taskCreated = properties.getEvents().getTaskCreated();
        this.title = Objects.requireNonNullElse(taskCreated.getTitle(), "Tarefa criada");
        this.color = Objects.requireNonNullElse(taskCreated.getColor(), 0x3498DB);
    }

    @Override
    protected Set<String> getRecipients(TaskCreatedEvent event) {
        User eventOwner = event.getTriggeredBy(); // Event owner is the task creator
        return Set.of(eventOwner.getId());
    }

    @Override
    protected List<DiscordWebhookPayload.Field> buildFields(TaskCreatedEvent event) {
        Task task = event.getTask();
        User eventOwner = event.getTriggeredBy();

        return new DiscordWebhookPayload.EmbedFieldsBuilder()
                .addField("Título", task.getTitle())
                .addField("Descrição", task.getDescription())
                .addField("Task ID", task.getId())
                .addField("Criado por", eventOwner.getNameWithEmail())
                .build();
    }
}
