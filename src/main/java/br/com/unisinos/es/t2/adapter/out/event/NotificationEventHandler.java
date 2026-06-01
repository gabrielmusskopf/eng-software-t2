package br.com.unisinos.es.t2.adapter.out.event;

import br.com.unisinos.es.t2.application.domain.model.TaskCreatedEvent;
import br.com.unisinos.es.t2.application.domain.model.TaskDeletedEvent;
import br.com.unisinos.es.t2.application.domain.model.TaskUpdatedEvent;
import br.com.unisinos.es.t2.application.port.in.task.TaskNotificationService;
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

    private final List<TaskNotificationService<TaskCreatedEvent>> taskCreatedNotificationServiceList;
    private final List<TaskNotificationService<TaskDeletedEvent>> taskDeletedNotificationServiceList;
    private final List<TaskNotificationService<TaskUpdatedEvent>> taskUpdatedNotificationServiceList;

    @EventListener(TaskCreatedEvent.class)
    public void handleTaskCreatedEvent(TaskCreatedEvent event) {
        log.debug("Handling TaskCreatedEvent for task id {}", event);
        taskCreatedNotificationServiceList.forEach(notificationService -> notificationService.notify(event));
    }

    @EventListener(TaskDeletedEvent.class)
    public void handleTaskDeletedEvent(TaskDeletedEvent event) {
        log.debug("Handling TaskDeletedEvent for task id {}", event);
        taskDeletedNotificationServiceList.forEach(notificationService -> notificationService.notify(event));
    }

    @EventListener(TaskUpdatedEvent.class)
    public void handleTaskUpdatedEvent(TaskUpdatedEvent event) {
        log.debug("Handling TaskUpdatedEvent for task id {}", event);
        taskUpdatedNotificationServiceList.forEach(notificationService -> notificationService.notify(event));
    }

    @PostConstruct
    void logCreation() {
        log.debug(
                "NotificationEventHandler created, notifications are enabled. Found "
                        + "{} TaskCreatedNotificationService beans, "
                        + "{} TaskDeletedNotificationService beans, "
                        + "{} TaskUpdatedNotificationService beans.",
                taskCreatedNotificationServiceList.size(),
                taskDeletedNotificationServiceList.size(),
                taskUpdatedNotificationServiceList.size());
    }
}
