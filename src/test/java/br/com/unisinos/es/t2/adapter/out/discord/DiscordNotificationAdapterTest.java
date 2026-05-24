package br.com.unisinos.es.t2.adapter.out.discord;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import br.com.unisinos.es.t2.application.domain.model.DiscordWebhookConfig;
import br.com.unisinos.es.t2.application.domain.model.Task;
import br.com.unisinos.es.t2.application.domain.model.TaskEvent;
import br.com.unisinos.es.t2.application.domain.model.TaskStatus;
import br.com.unisinos.es.t2.application.port.out.GetDiscordWebhookByUserIdPort;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DiscordNotificationAdapterTest {

    private final DiscordWebhookProperties properties = new DiscordWebhookProperties(true, "Task Manager", 3000, 5000);

    @Mock
    private GetDiscordWebhookByUserIdPort getDiscordWebhookByUserIdPort;

    @Mock
    private DiscordWebhookClient discordWebhookClient;

    private DiscordNotificationAdapter adapter;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        adapter = new DiscordNotificationAdapter(properties, getDiscordWebhookByUserIdPort, discordWebhookClient);
    }

    private static Task task() {
        return Task.builder()
                .id("task-1")
                .title("Some title")
                .description("desc")
                .status(TaskStatus.CREATED)
                .creatorId("creator-1")
                .assigneeId("assignee-1")
                .build();
    }

    @Test
    void notify_disabled_doesNothing() {
        DiscordWebhookProperties disabled = new DiscordWebhookProperties(false, "X", 1000, 1000);
        DiscordNotificationAdapter local =
                new DiscordNotificationAdapter(disabled, getDiscordWebhookByUserIdPort, discordWebhookClient);

        local.notify(TaskEvent.CREATED, task());

        verifyNoInteractions(getDiscordWebhookByUserIdPort);
        verifyNoInteractions(discordWebhookClient);
    }

    @Test
    void notify_created_sendsToCreatorOnly() {
        when(getDiscordWebhookByUserIdPort.getByUserId("creator-1"))
                .thenReturn(Optional.of(DiscordWebhookConfig.builder()
                        .userId("creator-1")
                        .webhookUrl("https://discord.com/hook/creator")
                        .build()));

        adapter.notify(TaskEvent.CREATED, task());

        verify(discordWebhookClient).send(eq("https://discord.com/hook/creator"), any(DiscordWebhookPayload.class));
        verify(getDiscordWebhookByUserIdPort, never()).getByUserId("assignee-1");
    }

    @Test
    void notify_assigned_sendsToAssigneeOnly() {
        when(getDiscordWebhookByUserIdPort.getByUserId("assignee-1"))
                .thenReturn(Optional.of(DiscordWebhookConfig.builder()
                        .userId("assignee-1")
                        .webhookUrl("https://discord.com/hook/assignee")
                        .build()));

        adapter.notify(TaskEvent.ASSIGNED, task());

        verify(discordWebhookClient).send(eq("https://discord.com/hook/assignee"), any(DiscordWebhookPayload.class));
        verify(getDiscordWebhookByUserIdPort, never()).getByUserId("creator-1");
    }

    @Test
    void notify_finished_sendsToCreatorAndAssignee() {
        when(getDiscordWebhookByUserIdPort.getByUserId("creator-1"))
                .thenReturn(Optional.of(DiscordWebhookConfig.builder()
                        .userId("creator-1")
                        .webhookUrl("https://creator")
                        .build()));
        when(getDiscordWebhookByUserIdPort.getByUserId("assignee-1"))
                .thenReturn(Optional.of(DiscordWebhookConfig.builder()
                        .userId("assignee-1")
                        .webhookUrl("https://assignee")
                        .build()));

        adapter.notify(TaskEvent.FINISHED, task());

        verify(discordWebhookClient).send(eq("https://creator"), any());
        verify(discordWebhookClient).send(eq("https://assignee"), any());
        verify(discordWebhookClient, times(2)).send(anyString(), any());
    }

    @Test
    void notify_noWebhookConfigured_skipsWithoutError() {
        when(getDiscordWebhookByUserIdPort.getByUserId("creator-1")).thenReturn(Optional.empty());

        adapter.notify(TaskEvent.CREATED, task());

        verifyNoInteractions(discordWebhookClient);
    }

    @Test
    void notify_clientThrows_swallowedAndOtherRecipientsStillCalled() {
        when(getDiscordWebhookByUserIdPort.getByUserId("creator-1"))
                .thenReturn(Optional.of(DiscordWebhookConfig.builder()
                        .userId("creator-1")
                        .webhookUrl("https://creator")
                        .build()));
        when(getDiscordWebhookByUserIdPort.getByUserId("assignee-1"))
                .thenReturn(Optional.of(DiscordWebhookConfig.builder()
                        .userId("assignee-1")
                        .webhookUrl("https://assignee")
                        .build()));
        org.mockito.Mockito.doThrow(new RuntimeException("boom"))
                .when(discordWebhookClient)
                .send(eq("https://creator"), any());

        adapter.notify(TaskEvent.FINISHED, task());

        verify(discordWebhookClient).send(eq("https://assignee"), any());
    }

    @Test
    void notify_payloadIncludesTaskFields() {
        when(getDiscordWebhookByUserIdPort.getByUserId("creator-1"))
                .thenReturn(Optional.of(DiscordWebhookConfig.builder()
                        .userId("creator-1")
                        .webhookUrl("https://x")
                        .build()));

        adapter.notify(TaskEvent.CREATED, task());

        ArgumentCaptor<DiscordWebhookPayload> captor = ArgumentCaptor.forClass(DiscordWebhookPayload.class);
        verify(discordWebhookClient).send(eq("https://x"), captor.capture());
        DiscordWebhookPayload payload = captor.getValue();
        assertThat(payload.embeds()).hasSize(1);
        assertThat(payload.embeds().get(0).title()).isEqualTo("Task created");
        assertThat(payload.embeds().get(0).fields())
                .extracting(DiscordWebhookPayload.Field::name)
                .contains("Title", "Status", "Creator", "Assignee", "Task ID");
    }
}
