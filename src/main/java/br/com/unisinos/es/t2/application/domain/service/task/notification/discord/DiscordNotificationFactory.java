package br.com.unisinos.es.t2.application.domain.service.task.notification.discord;

import br.com.unisinos.es.t2.application.domain.model.DiscordNotification;
import br.com.unisinos.es.t2.application.domain.model.DiscordWebhookPayload;
import br.com.unisinos.es.t2.application.domain.model.Task;
import br.com.unisinos.es.t2.application.domain.model.TaskCreatedEvent;
import br.com.unisinos.es.t2.application.domain.model.TaskDeletedEvent;
import br.com.unisinos.es.t2.application.domain.model.TaskEvent;
import br.com.unisinos.es.t2.application.domain.model.User;
import br.com.unisinos.es.t2.application.port.out.discordwebhook.DiscordWebhookProperties;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
        name = {"app.notifications.enabled", "app.notifications.discord.enabled"},
        havingValue = "true")
class DiscordNotificationFactory {

    private final DiscordWebhookProperties properties;

    public DiscordNotification newDiscordNotification(TaskEvent taskEvent) {
        return switch (taskEvent) {
            case TaskCreatedEvent taskCreatedEvent -> this.buildTaskCreatedNotification(taskCreatedEvent);
            case TaskDeletedEvent taskDeletedEvent -> this.buildTaskDeletedNotification(taskDeletedEvent);
        };
    }

    private DiscordNotification buildTaskCreatedNotification(TaskCreatedEvent event) {
        User eventOwner = event.getTriggeredBy(); // Event owner is the task creator
        Set<String> recipients = Set.of(eventOwner.getId());

        Task task = event.getTask();
        DiscordWebhookPayload.Embed embed = DiscordWebhookPayload.Embed.builder()
                .title("Nova tarefa criada")
                .color(0x3498DB)
                .description(task.getTitle())
                .fields(new DiscordWebhookPayload.EmbedFieldsBuilder()
                        .addField("Título", task.getTitle())
                        .addField("Descrição", task.getDescription())
                        .addField("Task ID", task.getId())
                        .addField("Criado por", eventOwner.getNameWithEmail())
                        .build())
                .build();

        return this.buildNotification(embed, recipients);
    }

    private DiscordNotification buildTaskDeletedNotification(TaskDeletedEvent event) {
        User assignedUser = event.getAssignee();
        User actionUser = event.getTriggeredBy();
        Set<String> recipients =
                Stream.of(assignedUser.getId(), actionUser.getId()).collect(Collectors.toSet());

        Task task = event.getTask();
        DiscordWebhookPayload.Embed embed = DiscordWebhookPayload.Embed.builder()
                .title("Tarefa removida")
                .color(0x3498DB)
                .description(task.getTitle())
                .fields(new DiscordWebhookPayload.EmbedFieldsBuilder()
                        .addField("Título", task.getTitle())
                        .addField("Descrição", task.getDescription())
                        .addField("Task ID", task.getId())
                        .addField("Removido por", actionUser.getNameWithEmail())
                        .addField("Vinculado a", assignedUser.getNameWithEmail())
                        .build())
                .build();

        return this.buildNotification(embed, recipients);
    }

    private DiscordNotification buildNotification(DiscordWebhookPayload.Embed embed, Set<String> recipients) {
        DiscordWebhookPayload payload = DiscordWebhookPayload.builder()
                .username(properties.getDefaultUsername())
                .embeds(List.of(embed))
                .build();

        return DiscordNotification.builder()
                .recipients(recipients)
                .payload(payload)
                .build();
    }

    @PostConstruct
    private void postConstruct() {
        log.debug(
                "{} initialized and ready to create Discord notifications",
                this.getClass().getSimpleName());
    }
}
