package br.com.unisinos.es.t2.application.domain.service.task;

import br.com.unisinos.es.t2.application.domain.model.TaskCreatedEvent;
import br.com.unisinos.es.t2.application.port.in.task.TaskCreatedNotificationService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
        name = {
            "app.notifications.enabled",
            "app.notifications.discord.enabled",
            "app.notifications.discord.events.task-created"
        },
        havingValue = "true")
class TaskCreatedDiscordNotification implements TaskCreatedNotificationService {

    @Override
    public void notify(TaskCreatedEvent event) {
        log.debug(
                "Notifying TaskCreatedEvent for task id {} via Discord",
                event.getTask().getId());
    }

    @PostConstruct
    void logCreation() {
        log.debug(
                "{} created, Discord notifications are enabled.",
                this.getClass().getSimpleName());
    }
}
