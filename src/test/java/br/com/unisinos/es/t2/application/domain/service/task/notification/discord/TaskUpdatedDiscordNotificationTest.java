package br.com.unisinos.es.t2.application.domain.service.task.notification.discord;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.unisinos.es.t2.EasyRandomExtension;
import br.com.unisinos.es.t2.application.domain.model.DiscordNotification;
import br.com.unisinos.es.t2.application.domain.model.DiscordWebhookPayload;
import br.com.unisinos.es.t2.application.domain.model.Task;
import br.com.unisinos.es.t2.application.domain.model.TaskReassignedEvent;
import br.com.unisinos.es.t2.application.domain.model.TaskTitleChangedEvent;
import br.com.unisinos.es.t2.application.domain.model.TaskUpdatedEvent;
import br.com.unisinos.es.t2.application.domain.model.User;
import br.com.unisinos.es.t2.application.port.out.discordwebhook.DiscordWebhookProperties;
import java.util.Iterator;
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
class TaskUpdatedDiscordNotificationTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private DiscordWebhookProperties properties;

    @Mock
    private DiscordNotificationService discordNotificationService;

    @Captor
    private ArgumentCaptor<DiscordNotification> notificationCaptor;

    @Test
    void shouldCreateTaskUpdatedNotificationWithTitleChangedEvent(EasyRandom easyRandom) {
        Task task = easyRandom.nextObject(Task.class);
        User user = easyRandom.nextObject(User.class);
        User assignee = easyRandom.nextObject(User.class);
        TaskTitleChangedEvent titleChangedEvent = new TaskTitleChangedEvent(task, user, assignee, "Old Title");
        TaskUpdatedEvent taskEvent = new TaskUpdatedEvent(task, user, assignee, List.of(titleChangedEvent));

        when(properties.getDefaultUsername()).thenReturn("Task Manager Bot");
        when(properties.getEvents().getTaskUpdated().getTitle()).thenReturn(null);
        when(properties.getEvents().getTaskUpdated().getColor()).thenReturn(null);

        TaskUpdatedDiscordNotification taskUpdatedDiscordNotification =
                new TaskUpdatedDiscordNotification(properties, discordNotificationService);
        taskUpdatedDiscordNotification.notify(taskEvent);

        verify(properties).getDefaultUsername();
        verify(discordNotificationService).notify(notificationCaptor.capture());

        DiscordNotification notification = notificationCaptor.getValue();

        assertEquals(2, notification.getRecipients().size());
        Iterator<String> iterator = notification.getRecipients().iterator();
        assertEquals(taskEvent.getAssignee().getId(), iterator.next());
        assertEquals(taskEvent.getTriggeredBy().getId(), iterator.next());

        assertEquals("Task Manager Bot", notification.getPayload().getUsername());
        assertEquals(1, notification.getPayload().getEmbeds().size());

        DiscordWebhookPayload.Embed embed =
                notification.getPayload().getEmbeds().getFirst();
        assertEquals("Tarefa atualizada", embed.getTitle());
        assertEquals(0x3498DB, embed.getColor());
        assertEquals(taskEvent.getTask().getTitle(), embed.getDescription());

        List<DiscordWebhookPayload.Field> fields = embed.getFields();
        assertEquals(6, fields.size());
        // Quando há mudança de título, os campos "Título anterior" e "Título novo" devem ser exibidos
        assertField(fields.get(0), "Título anterior", "Old Title");
        assertField(fields.get(1), "Título novo", task.getTitle());
        assertField(fields.get(2), "Descrição", task.getDescription());
        assertField(fields.get(3), "Task ID", task.getId());
        // Quando a tarefa é atualizada sem mudança de título, o campo "Título" deve ser zoomexibido
        assertField(fields.get(4), "Vinculado a", taskEvent.getAssignee().getNameWithEmail());
        assertField(fields.get(5), "Atualizada por", taskEvent.getTriggeredBy().getNameWithEmail());
    }

    @Test
    void shouldCreateTaskUpdatedNotificationWithTaskReassignedEvent(EasyRandom easyRandom) {
        Task task = easyRandom.nextObject(Task.class);
        User user = easyRandom.nextObject(User.class);
        User assigneeBefore = easyRandom.nextObject(User.class);
        User assigneeAfter = easyRandom.nextObject(User.class);
        TaskReassignedEvent taskReassignedEvent = new TaskReassignedEvent(task, user, assigneeBefore, assigneeAfter);
        TaskUpdatedEvent taskEvent = new TaskUpdatedEvent(task, user, assigneeAfter, List.of(taskReassignedEvent));

        when(properties.getDefaultUsername()).thenReturn("Task Manager Bot");
        when(properties.getEvents().getTaskUpdated().getTitle()).thenReturn(null);
        when(properties.getEvents().getTaskUpdated().getColor()).thenReturn(null);

        TaskUpdatedDiscordNotification taskUpdatedDiscordNotification =
                new TaskUpdatedDiscordNotification(properties, discordNotificationService);
        taskUpdatedDiscordNotification.notify(taskEvent);

        verify(properties).getDefaultUsername();
        verify(discordNotificationService).notify(notificationCaptor.capture());

        DiscordNotification notification = notificationCaptor.getValue();

        assertEquals(2, notification.getRecipients().size());
        assertTrue(notification.getRecipients().contains(taskEvent.getAssignee().getId()));
        assertTrue(
                notification.getRecipients().contains(taskEvent.getTriggeredBy().getId()));

        assertEquals("Task Manager Bot", notification.getPayload().getUsername());
        assertEquals(1, notification.getPayload().getEmbeds().size());

        DiscordWebhookPayload.Embed embed =
                notification.getPayload().getEmbeds().getFirst();
        assertEquals("Tarefa atualizada", embed.getTitle());
        assertEquals(0x3498DB, embed.getColor());
        assertEquals(taskEvent.getTask().getTitle(), embed.getDescription());

        List<DiscordWebhookPayload.Field> fields = embed.getFields();
        assertEquals(6, fields.size());
        // Quando não há mudança de título, o campo "Título" deve ser exibido
        assertField(fields.get(0), "Título", task.getTitle());
        assertField(fields.get(1), "Descrição", task.getDescription());
        assertField(fields.get(2), "Task ID", task.getId());
        // Quando a tarefa é reatribuída, os campos "De" e "Para" devem ser exibidos
        assertField(fields.get(3), "De", assigneeBefore.getNameWithEmail());
        assertField(fields.get(4), "Para", assigneeAfter.getNameWithEmail());
        assertField(fields.get(5), "Atualizada por", taskEvent.getTriggeredBy().getNameWithEmail());
    }

    private void assertField(DiscordWebhookPayload.Field field, String expectedName, String expectedValue) {
        assertEquals(expectedName, field.getName());
        assertEquals(expectedValue, field.getValue());
    }
}
