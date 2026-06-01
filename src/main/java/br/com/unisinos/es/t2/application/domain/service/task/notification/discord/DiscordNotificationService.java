package br.com.unisinos.es.t2.application.domain.service.task.notification.discord;

import br.com.unisinos.es.t2.application.domain.model.DiscordNotification;
import br.com.unisinos.es.t2.application.domain.model.DiscordWebhookConfig;
import br.com.unisinos.es.t2.application.domain.model.Task;
import br.com.unisinos.es.t2.application.port.out.discordwebhook.DiscordWebhookClient;
import br.com.unisinos.es.t2.application.port.out.discordwebhook.GetDiscordWebhookByUserIdPort;
import jakarta.annotation.PostConstruct;
import java.util.Optional;
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
class DiscordNotificationService {

    private final GetDiscordWebhookByUserIdPort getDiscordWebhookByUserIdPort;
    private final DiscordWebhookClient discordWebhookClient;

    public void notify(DiscordNotification discordNotification) {
        Task task = discordNotification.getTask();
        log.debug("Notifying TaskCreatedEvent for task id {} via Discord", task.getId());

        for (String userId : discordNotification.getRecipients()) {
            Optional<DiscordWebhookConfig> webhookConfigOptional = getDiscordWebhookByUserIdPort.getByUserId(userId);
            if (webhookConfigOptional.isEmpty()) {
                log.debug(
                        "No Discord webhook configured for user {}. Skipping notification for task id {}",
                        userId,
                        task.getId());
                continue;
            }

            DiscordWebhookConfig webhookConfig = webhookConfigOptional.get();
            try {
                discordWebhookClient.send(webhookConfig.getWebhookUrl(), discordNotification.getPayload());
                log.debug("Discord notification sent for task id {} to user {}", task.getId(), userId);
            } catch (Exception e) {
                // TODO: Consider retrying or marking the webhook as invalid after repeated failures
                log.warn(
                        "Failed to send Discord notification for task id {} to user {}: {}",
                        task.getId(),
                        userId,
                        e.getMessage());
            }
        }
    }

    @PostConstruct
    private void postConstruct() {
        log.debug(
                "{} initialized and ready to send Discord notifications",
                this.getClass().getSimpleName());
    }
}
