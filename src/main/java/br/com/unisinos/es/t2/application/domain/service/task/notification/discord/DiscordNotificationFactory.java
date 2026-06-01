package br.com.unisinos.es.t2.application.domain.service.task.notification.discord;

import br.com.unisinos.es.t2.application.domain.model.DiscordNotification;
import br.com.unisinos.es.t2.application.domain.model.DiscordWebhookPayload;
import br.com.unisinos.es.t2.application.domain.model.Task;
import br.com.unisinos.es.t2.application.domain.model.TaskEvent;
import br.com.unisinos.es.t2.application.port.out.discordwebhook.DiscordWebhookProperties;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
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
        return switch (taskEvent.getEventType()) {
            case CREATED -> this.buildTaskCreatedNotification(taskEvent);
        };
    }

    private DiscordNotification buildTaskCreatedNotification(TaskEvent event) {
        Task task = event.getTask();
        Set<String> recipients = Set.of(task.getUserId());

        DiscordWebhookPayload.Embed embed = DiscordWebhookPayload.Embed.builder()
                .title("Nova tarefa criada")
                .color(0x3498DB)
                .description(task.getTitle())
                .fields(new DiscordWebhookPayload.EmbedFieldsBuilder()
                        .addField("Title", task.getTitle())
                        .addField("Description", task.getDescription())
                        .addFieldInline("Creator ID", task.getUserId())
                        .addField("Task ID", task.getId())
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
