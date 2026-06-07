package br.com.unisinos.es.t2.application.domain.service.task.notification.discord;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.unisinos.es.t2.EasyRandomExtension;
import br.com.unisinos.es.t2.application.domain.model.DiscordNotification;
import br.com.unisinos.es.t2.application.domain.model.DiscordWebhookPayload;
import br.com.unisinos.es.t2.application.domain.model.TaskDeletedEvent;
import br.com.unisinos.es.t2.application.domain.model.User;
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
class TaskDeletedDiscordNotificationTest {

    private TaskDeletedDiscordNotification taskDeletedDiscordNotification;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private DiscordWebhookProperties properties;

    @Mock
    private DiscordNotificationService discordNotificationService;

    @Captor
    private ArgumentCaptor<DiscordNotification> notificationCaptor;

    @Test
    void shouldCreateTaskDeletedNotification(EasyRandom easyRandom) {
        TaskDeletedEvent taskEvent = easyRandom.nextObject(TaskDeletedEvent.class);
        User assignee = taskEvent.getAssignee();
        User triggeredBy = taskEvent.getTriggeredBy();

        when(properties.getDefaultUsername()).thenReturn("Task Manager Bot");
        when(properties.getEvents().getTaskDeleted().getColor()).thenReturn(null);
        doNothing().when(discordNotificationService).notify(any());

        taskDeletedDiscordNotification = new TaskDeletedDiscordNotification(properties, discordNotificationService);
        taskDeletedDiscordNotification.notify(taskEvent);

        verify(properties).getDefaultUsername();
        verify(discordNotificationService).notify(notificationCaptor.capture());

        DiscordNotification notification = notificationCaptor.getValue();

        assertEquals(2, notification.getRecipients().size());
        assertTrue(notification.getRecipients().contains(assignee.getId()));
        assertTrue(notification.getRecipients().contains(triggeredBy.getId()));

        assertEquals("Task Manager Bot", notification.getPayload().getUsername());
        assertEquals(1, notification.getPayload().getEmbeds().size());

        DiscordWebhookPayload.Embed embed =
                notification.getPayload().getEmbeds().getFirst();
        assertEquals("Tarefa deletada", embed.getTitle());
        assertEquals(0xE74C3C, embed.getColor());
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

    @Test
    void shouldCreateTaskDeletedNotificationWithParametersFromProperties(EasyRandom easyRandom) {
        TaskDeletedEvent taskEvent = easyRandom.nextObject(TaskDeletedEvent.class);

        when(properties.getDefaultUsername()).thenReturn("Task Manager Bot");
        when(properties.getEvents().getTaskDeleted().getTitle()).thenReturn("Tarefa removida");
        when(properties.getEvents().getTaskDeleted().getColor()).thenReturn(0xFF5733);
        doNothing().when(discordNotificationService).notify(any());

        taskDeletedDiscordNotification = new TaskDeletedDiscordNotification(properties, discordNotificationService);
        taskDeletedDiscordNotification.notify(taskEvent);

        verify(properties).getDefaultUsername();
        verify(properties.getEvents().getTaskDeleted()).getTitle();
        verify(properties.getEvents().getTaskDeleted()).getColor();
        verify(discordNotificationService).notify(notificationCaptor.capture());

        DiscordNotification notification = notificationCaptor.getValue();

        assertEquals("Task Manager Bot", notification.getPayload().getUsername());
        assertEquals(1, notification.getPayload().getEmbeds().size());

        DiscordWebhookPayload.Embed embed =
                notification.getPayload().getEmbeds().getFirst();
        assertEquals("Tarefa removida", embed.getTitle());
        assertEquals(0xFF5733, embed.getColor());
    }
}
