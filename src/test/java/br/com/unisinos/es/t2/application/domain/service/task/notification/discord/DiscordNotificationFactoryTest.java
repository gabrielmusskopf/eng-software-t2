package br.com.unisinos.es.t2.application.domain.service.task.notification.discord;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import br.com.unisinos.es.t2.EasyRandomExtension;
import br.com.unisinos.es.t2.application.domain.model.DiscordNotification;
import br.com.unisinos.es.t2.application.domain.model.DiscordWebhookPayload;
import br.com.unisinos.es.t2.application.domain.model.TaskCreatedEvent;
import br.com.unisinos.es.t2.application.domain.model.TaskDeletedEvent;
import br.com.unisinos.es.t2.application.domain.model.User;
import br.com.unisinos.es.t2.application.port.out.discordwebhook.DiscordWebhookProperties;
import java.util.Iterator;
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
                taskEvent.getTriggeredBy().getId(),
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
        assertEquals("Título", fields.get(0).getName());
        assertEquals(taskEvent.getTask().getTitle(), fields.get(0).getValue());
        assertEquals("Descrição", fields.get(1).getName());
        assertEquals(taskEvent.getTask().getDescription(), fields.get(1).getValue());
        assertEquals("Task ID", fields.get(2).getName());
        assertEquals(taskEvent.getTask().getId(), fields.get(2).getValue());
        assertEquals("Criado por", fields.get(3).getName());
        assertEquals(
                taskEvent.getTriggeredBy().getNameWithEmail(), fields.get(3).getValue());
    }

    @Test
    void shouldCreateTaskDeletedNotification(EasyRandom easyRandom) {
        TaskDeletedEvent taskEvent = easyRandom.nextObject(TaskDeletedEvent.class);
        User assignee = taskEvent.getAssignee();
        User triggeredBy = taskEvent.getTriggeredBy();

        when(properties.getDefaultUsername()).thenReturn("Task Manager Bot");

        DiscordNotification notification = discordNotificationFactory.newDiscordNotification(taskEvent);

        assertEquals(2, notification.getRecipients().size());
        Iterator<String> iterator = notification.getRecipients().iterator();
        assertEquals(assignee.getId(), iterator.next());
        assertEquals(triggeredBy.getId(), iterator.next());

        assertEquals("Task Manager Bot", notification.getPayload().getUsername());
        assertEquals(1, notification.getPayload().getEmbeds().size());

        DiscordWebhookPayload.Embed embed =
                notification.getPayload().getEmbeds().getFirst();
        assertEquals("Tarefa removida", embed.getTitle());
        assertEquals(0x3498DB, embed.getColor());
        assertEquals(taskEvent.getTask().getTitle(), embed.getDescription());

        List<DiscordWebhookPayload.Field> fields = embed.getFields();
        assertEquals(5, fields.size());
        assertEquals("Título", fields.get(0).getName());
        assertEquals(taskEvent.getTask().getTitle(), fields.get(0).getValue());
        assertEquals("Descrição", fields.get(1).getName());
        assertEquals(taskEvent.getTask().getDescription(), fields.get(1).getValue());
        assertEquals("Task ID", fields.get(2).getName());
        assertEquals(taskEvent.getTask().getId(), fields.get(2).getValue());
        assertEquals("Removido por", fields.get(3).getName());
        assertEquals(triggeredBy.getNameWithEmail(), fields.get(3).getValue());
        assertEquals("Vinculado a", fields.get(4).getName());
        assertEquals(assignee.getNameWithEmail(), fields.get(4).getValue());
    }
}
