package br.com.unisinos.es.t2.adapter.out.event;

import br.com.unisinos.es.t2.application.domain.model.TaskCreatedEvent;
import br.com.unisinos.es.t2.application.port.in.task.TaskCreatedNotificationService;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.notifications.enabled", havingValue = "true")
class NotificationEventHandler {

    private final List<TaskCreatedNotificationService> taskCreatedNotificationServiceList;

    @EventListener(TaskCreatedEvent.class)
    public void handleTaskCreatedEvent(TaskCreatedEvent event) {
        log.debug("Handling TaskCreatedEvent for task id {}", event);
        taskCreatedNotificationServiceList.forEach(notificationService -> notificationService.notify(event));
    }

    @PostConstruct
    void logCreation() {
        log.debug(
                "NotificationEventHandler created, notifications are enabled. "
                        + "Found {} TaskCreatedNotificationService beans",
                taskCreatedNotificationServiceList.size());
    }
}
