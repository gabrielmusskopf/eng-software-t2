package br.com.unisinos.es.t2.application.domain.service.task.notification.discord;

import br.com.unisinos.es.t2.application.domain.model.TaskDeletedEvent;
import br.com.unisinos.es.t2.application.port.in.task.TaskDeletedNotificationService;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Getter
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
        name = {
            "app.notifications.enabled",
            "app.notifications.discord.enabled",
            "app.notifications.discord.events.task-deleted.enabled"
        },
        havingValue = "true")
class TaskDeletedDiscordNotification implements TaskDeletedNotificationService {

    private final DiscordNotificationService discordNotificationService;

    @Override
    public void notify(TaskDeletedEvent event) {
        log.debug(
                "Notifying TaskDeletedEvent for task id {} via Discord",
                event.getTask().getId());

        discordNotificationService.notify(event);
    }

    @PostConstruct
    private void postConstruct() {
        log.debug(
                "{} initialized and ready to send notifications for TaskDeletedEvent",
                this.getClass().getSimpleName());
    }
}
