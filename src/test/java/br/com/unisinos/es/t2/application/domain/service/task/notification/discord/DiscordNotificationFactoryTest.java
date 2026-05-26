package br.com.unisinos.es.t2.application.domain.service.task.notification.discord;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import br.com.unisinos.es.t2.EasyRandomExtension;
import br.com.unisinos.es.t2.application.domain.model.DiscordNotification;
import br.com.unisinos.es.t2.application.domain.model.DiscordWebhookPayload;
import br.com.unisinos.es.t2.application.domain.model.TaskCreatedEvent;
import br.com.unisinos.es.t2.application.port.out.discordwebhook.DiscordWebhookProperties;
import java.util.List;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(EasyRandomExtension.class)
class DiscordNotificationFactoryTest {

    @InjectMocks
    private DiscordNotificationFactory discordNotificationFactory;

    @Mock
    private DiscordWebhookProperties properties;

    @Test
    void shouldCreateTaskCreatedNotification(EasyRandom easyRandom) {

        TaskCreatedEvent taskEvent = easyRandom.nextObject(TaskCreatedEvent.class);
        when(properties.getDefaultUsername()).thenReturn("Task Manager Bot");

        DiscordNotification notification = discordNotificationFactory.newDiscordNotification(taskEvent);

        assertEquals(1, notification.getRecipients().size());
        assertEquals(
                taskEvent.getTask().getUserId(),
                notification.getRecipients().iterator().next());

        assertEquals("Task Manager Bot", notification.getPayload().getUsername());
        assertEquals(1, notification.getPayload().getEmbeds().size());

        DiscordWebhookPayload.Embed embed =
                notification.getPayload().getEmbeds().getFirst();
        assertEquals("Nova tarefa criada", embed.getTitle());
        assertEquals(0x3498DB, embed.getColor());
        assertEquals(taskEvent.getTask().getTitle(), embed.getDescription());

        List<DiscordWebhookPayload.Field> fields = embed.getFields();
        assertEquals(4, fields.size());
        assertEquals("Title", fields.get(0).getName());
        assertEquals(taskEvent.getTask().getTitle(), fields.get(0).getValue());
        assertEquals("Description", fields.get(1).getName());
        assertEquals(taskEvent.getTask().getDescription(), fields.get(1).getValue());
        assertEquals("Creator ID", fields.get(2).getName());
        assertEquals(taskEvent.getTask().getUserId(), fields.get(2).getValue());
        assertEquals("Task ID", fields.get(3).getName());
        assertEquals(taskEvent.getTask().getId(), fields.get(3).getValue());
    }
}
