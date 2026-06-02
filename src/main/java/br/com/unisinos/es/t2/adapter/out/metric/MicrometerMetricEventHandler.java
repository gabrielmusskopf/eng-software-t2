package br.com.unisinos.es.t2.adapter.out.metric;

import br.com.unisinos.es.t2.application.domain.model.TaskCreatedEvent;
import br.com.unisinos.es.t2.application.domain.model.TaskDeletedEvent;
import br.com.unisinos.es.t2.application.domain.model.TaskDescriptionChangedEvent;
import br.com.unisinos.es.t2.application.domain.model.TaskReassignedEvent;
import br.com.unisinos.es.t2.application.domain.model.TaskStatus;
import br.com.unisinos.es.t2.application.domain.model.TaskStatusChangedEvent;
import br.com.unisinos.es.t2.application.domain.model.TaskTitleChangedEvent;
import br.com.unisinos.es.t2.application.port.out.task.CountTaskByStatusPort;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
        name = {"app.metrics.enabled"},
        havingValue = "true")
class MicrometerMetricEventHandler {

    private static final String TASK_OPERATIONS_METRIC_NAME = "api_task_operations_total";

    private final MeterRegistry meterRegistry;
    private final CountTaskByStatusPort countTaskByStatusPort;

    @EventListener(TaskCreatedEvent.class)
    public void handleTaskCreated(TaskCreatedEvent event) {
        log.debug(
                "Handling TaskCreatedEvent for task with id: {}",
                event.getTask().getId());
        meterRegistry
                .counter(TASK_OPERATIONS_METRIC_NAME, "operation", "create", "description", "Criado")
                .increment();
    }

    @EventListener(TaskDeletedEvent.class)
    public void handleTaskDeleted(TaskDeletedEvent event) {
        log.debug(
                "Handling TaskDeletedEvent for task with id: {}",
                event.getTask().getId());
        meterRegistry
                .counter(TASK_OPERATIONS_METRIC_NAME, "operation", "delete", "description", "Excluído")
                .increment();
    }

    @EventListener(TaskReassignedEvent.class)
    public void handleTaskReassigned(TaskReassignedEvent event) {
        log.debug(
                "Handling TaskReassignedEvent for task with id: {}, from user id: {} to user id: {}",
                event.getTask().getId(),
                event.getAssigneeBefore().getId(),
                event.getAssigneeAfter().getId());
        meterRegistry
                .counter(TASK_OPERATIONS_METRIC_NAME, "operation", "reassign", "description", "Re-atribuído")
                .increment();
    }

    @EventListener(TaskTitleChangedEvent.class)
    public void handleTaskTitleChanged(TaskTitleChangedEvent event) {
        log.debug(
                "Handling TaskTitleChangedEvent for task with id: {}, from title: {} to title: {}",
                event.getTask().getId(),
                event.getTitleBefore(),
                event.getTask().getTitle());
        meterRegistry
                .counter(TASK_OPERATIONS_METRIC_NAME, "operation", "update_title", "description", "Título atualizado")
                .increment();
    }

    @EventListener(TaskDescriptionChangedEvent.class)
    public void handleTaskDescriptionChanged(TaskDescriptionChangedEvent event) {
        log.debug(
                "Handling TaskDescriptionChangedEvent for task with id: {}, from description: {} to description: {}",
                event.getTask().getId(),
                event.getDescriptionBefore(),
                event.getTask().getDescription());
        meterRegistry
                .counter(
                        TASK_OPERATIONS_METRIC_NAME,
                        "operation",
                        "update_description",
                        "description",
                        "Descrição atualizada")
                .increment();
    }

    @EventListener(TaskStatusChangedEvent.class)
    public void handleTaskStatusChanged(TaskStatusChangedEvent event) {
        log.debug(
                "Handling TaskStatusChangedEvent for task with id: {}, from status: {} to status: {}",
                event.getTask().getId(),
                event.getStatusBefore(),
                event.getTask().getStatus());

        final String displayName = event.getTask().getStatus().getDisplayName();
        switch (event.getTask().getStatus()) {
            case BACKLOG ->
                meterRegistry
                        .counter(TASK_OPERATIONS_METRIC_NAME, "operation", "deferred", "description", displayName)
                        .increment();
            case IN_PROGRESS ->
                meterRegistry
                        .counter(TASK_OPERATIONS_METRIC_NAME, "operation", "started", "description", displayName)
                        .increment();
            case COMPLETED ->
                meterRegistry
                        .counter(TASK_OPERATIONS_METRIC_NAME, "operation", "completed", "description", displayName)
                        .increment();
            case CANCELLED ->
                meterRegistry
                        .counter(TASK_OPERATIONS_METRIC_NAME, "operation", "cancelled", "description", displayName)
                        .increment();
        }

        Duration timeInStatus = Duration.between(event.getStatusLastChangedAt(), LocalDateTime.now());
        Timer.builder("api_task_status_duration_seconds")
                .tag("status", event.getStatusBefore().name().toLowerCase())
                .tag("description", event.getStatusBefore().getDisplayName())
                .description("Tempo médio gasto em cada status")
                .register(meterRegistry)
                .record(timeInStatus);
    }

    @PostConstruct
    void registerGaugesTasksByStatus() {
        for (TaskStatus status : TaskStatus.values()) {
            log.debug("Registering Gauge for task status: {}", status);
            // TODO: Refactor to avoid querying the database on every scrape. Consider using a cache or a scheduled task to update the counts periodically.
            Gauge.builder("api_tasks_by_status", status, countTaskByStatusPort::countByStatus)
                    .description("Número atual de tarefas por status")
                    .tag("status", status.name().toLowerCase())
                    .tag("description", status.getDisplayName())
                    .register(meterRegistry);
        }
    }

    @PostConstruct
    void postConstruct() {
        log.debug("MicrometerMetricEventHandler initialized and ready to handle events.");
    }
}
