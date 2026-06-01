package br.com.unisinos.es.t2.application.domain.service.task.notification.discord;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
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
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(EasyRandomExtension.class)
class TaskCreatedDiscordNotificationTest {

    private TaskCreatedDiscordNotification taskCreatedDiscordNotification;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private DiscordWebhookProperties properties;

    @Mock
    private DiscordNotificationService discordNotificationService;

    @Captor
    private ArgumentCaptor<DiscordNotification> notificationCaptor;

    @Test
    void shouldCreateTaskCreatedNotification(EasyRandom easyRandom) {
        TaskCreatedEvent taskEvent = easyRandom.nextObject(TaskCreatedEvent.class);

        when(properties.getDefaultUsername()).thenReturn("Task Manager Bot");
        when(properties.getEvents().getTaskCreated().getTitle()).thenReturn(null);
        when(properties.getEvents().getTaskCreated().getColor()).thenReturn(null);
        doNothing().when(discordNotificationService).notify(any());

        taskCreatedDiscordNotification = new TaskCreatedDiscordNotification(properties, discordNotificationService);
        taskCreatedDiscordNotification.notify(taskEvent);

        verify(properties).getDefaultUsername();
        verify(discordNotificationService).notify(notificationCaptor.capture());

        DiscordNotification notification = notificationCaptor.getValue();

        assertEquals(1, notification.getRecipients().size());
        assertEquals(
                taskEvent.getTriggeredBy().getId(),
                notification.getRecipients().iterator().next());

        assertEquals("Task Manager Bot", notification.getPayload().getUsername());
        assertEquals(1, notification.getPayload().getEmbeds().size());

        DiscordWebhookPayload.Embed embed =
                notification.getPayload().getEmbeds().getFirst();
        assertEquals("Tarefa criada", embed.getTitle());
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
    void shouldCreateTaskCreatedNotificationWithParametersFromProperties(EasyRandom easyRandom) {
        TaskCreatedEvent taskEvent = easyRandom.nextObject(TaskCreatedEvent.class);

        when(properties.getDefaultUsername()).thenReturn("Custom Bot");
        when(properties.getEvents().getTaskCreated().getTitle()).thenReturn("Custom Task Created");
        when(properties.getEvents().getTaskCreated().getColor()).thenReturn(0xFF5733);
        doNothing().when(discordNotificationService).notify(any());

        taskCreatedDiscordNotification = new TaskCreatedDiscordNotification(properties, discordNotificationService);
        taskCreatedDiscordNotification.notify(taskEvent);

        verify(properties).getDefaultUsername();
        verify(properties.getEvents().getTaskCreated()).getTitle();
        verify(properties.getEvents().getTaskCreated()).getColor();
        verify(discordNotificationService).notify(notificationCaptor.capture());

        DiscordNotification notification = notificationCaptor.getValue();

        assertEquals("Custom Bot", notification.getPayload().getUsername());
        assertEquals(1, notification.getPayload().getEmbeds().size());

        DiscordWebhookPayload.Embed embed =
                notification.getPayload().getEmbeds().getFirst();
        assertEquals("Custom Task Created", embed.getTitle());
        assertEquals(0xFF5733, embed.getColor());
    }
}
