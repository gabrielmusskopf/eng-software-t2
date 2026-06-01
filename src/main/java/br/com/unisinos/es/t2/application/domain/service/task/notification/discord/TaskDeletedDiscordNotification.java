package br.com.unisinos.es.t2.application.domain.service.task.notification.discord;

import br.com.unisinos.es.t2.application.domain.model.DiscordWebhookPayload;
import br.com.unisinos.es.t2.application.domain.model.Task;
import br.com.unisinos.es.t2.application.domain.model.TaskDeletedEvent;
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
            "app.notifications.discord.events.task-deleted.enabled"
        },
        havingValue = "true")
class TaskDeletedDiscordNotification extends AbstractDiscordNotification<TaskDeletedEvent> {

    private final String title;
    private final int color;

    public TaskDeletedDiscordNotification(
            DiscordWebhookProperties properties, DiscordNotificationService discordNotificationService) {
        super(properties, discordNotificationService);

        DiscordWebhookProperties.Event taskDeleted = properties.getEvents().getTaskDeleted();
        this.title = Objects.requireNonNullElse(taskDeleted.getTitle(), "Tarefa deletada");
        this.color = Objects.requireNonNullElse(taskDeleted.getColor(), 0xE74C3C);
    }

    @Override
    protected Set<String> getRecipients(TaskDeletedEvent event) {
        User assignedUser = event.getAssignee();
        User actionUser = event.getTriggeredBy();
        return Stream.of(assignedUser.getId(), actionUser.getId()).collect(Collectors.toSet());
    }

    @Override
    protected List<DiscordWebhookPayload.Field> buildFields(TaskDeletedEvent event) {
        Task task = event.getTask();
        User actionUser = event.getTriggeredBy();

        return new DiscordWebhookPayload.EmbedFieldsBuilder()
                .addField("Título", task.getTitle())
                .addField("Descrição", task.getDescription())
                .addField("Task ID", task.getId())
                .addField("Removido por", actionUser.getNameWithEmail())
                .addField("Vinculado a", event.getAssignee().getNameWithEmail())
                .build();
    }
}
