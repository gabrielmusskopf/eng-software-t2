package br.com.unisinos.es.t2.application.domain.service.task.notification.discord;

import br.com.unisinos.es.t2.application.domain.model.DiscordNotification;
import br.com.unisinos.es.t2.application.domain.model.DiscordWebhookPayload;
import br.com.unisinos.es.t2.application.domain.model.Task;
import br.com.unisinos.es.t2.application.domain.model.TaskEvent;
import br.com.unisinos.es.t2.application.port.in.task.TaskNotificationService;
import br.com.unisinos.es.t2.application.port.out.discordwebhook.DiscordWebhookProperties;
import jakarta.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

/**
 * Abstract base class for Discord notifications related to task events.
 * This class provides common logic for building and sending Discord notifications,
 * while allowing subclasses to specify the details of the notification such as the
 * title, color, recipients, and additional fields based on the specific event type.
 * @param <E> the type of TaskEvent that this notification will handle
 */
@Slf4j
abstract class AbstractDiscordNotification<E extends TaskEvent> implements TaskNotificationService<E> {

    protected final DiscordWebhookProperties properties;
    protected final DiscordNotificationService discordNotificationService;

    AbstractDiscordNotification(
            DiscordWebhookProperties properties, DiscordNotificationService discordNotificationService) {
        this.properties = properties;
        this.discordNotificationService = discordNotificationService;
    }

    @Override
    public final void notify(E event) {
        log.debug(
                "Notifying {} for task id {} via Discord",
                this.getEventTypeName(),
                event.getTask().getId());

        DiscordNotification discordNotification = this.buildNotification(event);
        discordNotificationService.notify(discordNotification);
    }

    protected abstract String getTitle();

    protected abstract int getColor();

    /**
     * Subclasses must implement this method to determine the recipients of the notification based on the event.
     * @return A set of user IDs that should receive the notification for the given event.
     */
    protected abstract Set<String> getRecipients(E event);

    /**
     * By default, the username for the Discord notification is taken from the properties.
     * Subclasses can override this method to provide a dynamic username based on the event if needed.
     */
    protected String getUsername(E event) {
        return properties.getDefaultUsername();
    }

    /**
     * By default, no additional fields are added to the embed.
     * Subclasses can override this method to provide custom fields based on the event.
     */
    protected List<DiscordWebhookPayload.Field> buildFields(E event) {
        return List.of();
    }

    protected DiscordNotification buildNotification(E event) {
        Task task = event.getTask();

        DiscordWebhookPayload.Embed embed = DiscordWebhookPayload.Embed.builder()
                .title(getTitle())
                .color(getColor())
                .description(task.getTitle())
                .fields(buildFields(event))
                .build();

        DiscordWebhookPayload payload = DiscordWebhookPayload.builder()
                .username(getUsername(event))
                .embeds(List.of(embed))
                .build();

        return DiscordNotification.builder()
                .task(task)
                .recipients(getRecipients(event))
                .payload(payload)
                .build();
    }

    @PostConstruct
    private void postConstruct() {
        log.debug(
                "{} initialized and ready to send notifications for {}",
                this.getClass().getSimpleName(),
                this.getEventTypeName());
    }

    private String getEventTypeName() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        return ((Class<?>) genericSuperclass.getActualTypeArguments()[0]).getSimpleName();
    }
}
