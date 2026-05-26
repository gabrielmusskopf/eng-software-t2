package br.com.unisinos.es.t2.application.domain.service.task.notification.discord;

import br.com.unisinos.es.t2.application.domain.model.TaskCreatedEvent;
import br.com.unisinos.es.t2.application.port.in.task.TaskCreatedNotificationService;
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
            "app.notifications.discord.events.task-created.enabled"
        },
        havingValue = "true")
class TaskCreatedDiscordNotification implements TaskCreatedNotificationService {

    private final DiscordNotificationService discordNotificationService;

    @Override
    public void notify(TaskCreatedEvent event) {
        log.debug(
                "Notifying TaskCreatedEvent for task id {} via Discord",
                event.getTask().getId());

        discordNotificationService.notify(event);
    }

    @PostConstruct
    private void postConstruct() {
        log.debug(
                "{} initialized and ready to send notifications for TaskCreatedEvent",
                this.getClass().getSimpleName());
    }
}
