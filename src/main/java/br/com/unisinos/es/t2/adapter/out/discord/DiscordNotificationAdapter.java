package br.com.unisinos.es.t2.adapter.out.discord;

import br.com.unisinos.es.t2.application.domain.model.DiscordWebhookConfig;
import br.com.unisinos.es.t2.application.domain.model.Task;
import br.com.unisinos.es.t2.application.domain.model.TaskEvent;
import br.com.unisinos.es.t2.application.port.out.GetDiscordWebhookByUserIdPort;
import br.com.unisinos.es.t2.application.port.out.NotifyTaskEventPort;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class DiscordNotificationAdapter implements NotifyTaskEventPort {

    private static final int COLOR_CREATED = 0x3498DB;
    private static final int COLOR_ASSIGNED = 0xF1C40F;
    private static final int COLOR_FINISHED = 0x2ECC71;

    private final DiscordWebhookProperties properties;
    private final GetDiscordWebhookByUserIdPort getDiscordWebhookByUserIdPort;
    private final DiscordWebhookClient discordWebhookClient;

    @Override
    public void notify(TaskEvent event, Task task) {
        if (!properties.enabled()) {
            log.debug("Discord notifications disabled — skipping {} for task {}", event, task.getId());
            return;
        }
        Set<String> recipients = recipientsFor(event, task);
        if (recipients.isEmpty()) {
            return;
        }
        DiscordWebhookPayload payload = buildPayload(event, task);
        for (String userId : recipients) {
            Optional<DiscordWebhookConfig> config = getDiscordWebhookByUserIdPort.getByUserId(userId);
            if (config.isEmpty()) {
                log.debug("No Discord webhook configured for user {} — skipping {}", userId, event);
                continue;
            }
            DiscordWebhookConfig cfg = config.get();
            DiscordWebhookPayload finalPayload = payload.username() != null
                    ? payload
                    : new DiscordWebhookPayload(
                            payload.content(),
                            cfg.getUsername() != null ? cfg.getUsername() : properties.defaultUsername(),
                            payload.embeds());
            try {
                discordWebhookClient.send(cfg.getWebhookUrl(), finalPayload);
            } catch (Exception e) {
                log.warn(
                        "Failed to deliver Discord notification for user {} (event {}): {}",
                        userId,
                        event,
                        e.getMessage());
            }
        }
    }

    private Set<String> recipientsFor(TaskEvent event, Task task) {
        Set<String> result = new LinkedHashSet<>();
        switch (event) {
            case CREATED -> addIfPresent(result, task.getCreatorId());
            case ASSIGNED -> addIfPresent(result, task.getAssigneeId());
            case FINISHED -> {
                addIfPresent(result, task.getCreatorId());
                addIfPresent(result, task.getAssigneeId());
            }
        }
        return result;
    }

    private static void addIfPresent(Set<String> set, String value) {
        if (value != null && !value.isBlank()) {
            set.add(value);
        }
    }

    private DiscordWebhookPayload buildPayload(TaskEvent event, Task task) {
        String title =
                switch (event) {
                    case CREATED -> "Task created";
                    case ASSIGNED -> "Task assigned";
                    case FINISHED -> "Task finished";
                };
        int color =
                switch (event) {
                    case CREATED -> COLOR_CREATED;
                    case ASSIGNED -> COLOR_ASSIGNED;
                    case FINISHED -> COLOR_FINISHED;
                };

        List<DiscordWebhookPayload.Field> fields = new ArrayList<>();
        fields.add(new DiscordWebhookPayload.Field("Title", task.getTitle(), false));
        if (task.getDescription() != null && !task.getDescription().isBlank()) {
            fields.add(new DiscordWebhookPayload.Field("Description", task.getDescription(), false));
        }
        if (task.getStatus() != null) {
            fields.add(
                    new DiscordWebhookPayload.Field("Status", task.getStatus().name(), true));
        }
        if (task.getCreatorId() != null) {
            fields.add(new DiscordWebhookPayload.Field("Creator", task.getCreatorId(), true));
        }
        if (task.getAssigneeId() != null) {
            fields.add(new DiscordWebhookPayload.Field("Assignee", task.getAssigneeId(), true));
        }
        if (task.getId() != null) {
            fields.add(new DiscordWebhookPayload.Field("Task ID", task.getId(), false));
        }

        DiscordWebhookPayload.Embed embed = DiscordWebhookPayload.Embed.builder()
                .title(title)
                .description(task.getTitle())
                .color(color)
                .fields(fields)
                .build();

        return DiscordWebhookPayload.builder()
                .username(properties.defaultUsername())
                .embeds(List.of(embed))
                .build();
    }
}
