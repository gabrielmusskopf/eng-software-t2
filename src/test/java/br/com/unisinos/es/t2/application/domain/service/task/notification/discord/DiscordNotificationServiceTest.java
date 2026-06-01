package br.com.unisinos.es.t2.application.domain.service.task.notification.discord;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import br.com.unisinos.es.t2.EasyRandomExtension;
import br.com.unisinos.es.t2.application.domain.model.DiscordNotification;
import br.com.unisinos.es.t2.application.domain.model.DiscordWebhookConfig;
import br.com.unisinos.es.t2.application.domain.model.DiscordWebhookPayload;
import br.com.unisinos.es.t2.application.port.out.discordwebhook.DiscordWebhookClient;
import br.com.unisinos.es.t2.application.port.out.discordwebhook.GetDiscordWebhookByUserIdPort;
import java.util.Optional;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(EasyRandomExtension.class)
class DiscordNotificationServiceTest {

    @InjectMocks
    private DiscordNotificationService notificationService;

    @Mock
    private GetDiscordWebhookByUserIdPort getDiscordWebhookByUserIdPort;

    @Mock
    private DiscordWebhookClient discordWebhookClient;

    @Test
    void shouldNotSendDiscordNotificationWhenRecipientsHaveNoWebhookConfigured(EasyRandom easyRandom) {
        DiscordNotification notification = easyRandom.nextObject(DiscordNotification.class);

        when(getDiscordWebhookByUserIdPort.getByUserId(anyString())).thenReturn(Optional.empty());

        notificationService.notify(notification);

        for (String userId : notification.getRecipients()) {
            verify(getDiscordWebhookByUserIdPort).getByUserId(userId);
            verifyNoInteractions(discordWebhookClient);
        }
    }

    @Test
    void shouldSendDiscordNotificationToAllRecipientsWithWebhookConfigured(EasyRandom easyRandom) {
        DiscordNotification notification = easyRandom.nextObject(DiscordNotification.class);

        when(getDiscordWebhookByUserIdPort.getByUserId(anyString()))
                .thenReturn(Optional.of(easyRandom.nextObject(DiscordWebhookConfig.class)));

        notificationService.notify(notification);

        int recipients = notification.getRecipients().size();
        verify(getDiscordWebhookByUserIdPort, times(recipients)).getByUserId(anyString());
        verify(discordWebhookClient, times(recipients)).send(anyString(), any(DiscordWebhookPayload.class));
    }

    @Test
    void shouldHandleDiscordNotificationFailureGracefully(EasyRandom easyRandom) {
        DiscordNotification notification = easyRandom.nextObject(DiscordNotification.class);

        when(getDiscordWebhookByUserIdPort.getByUserId(anyString()))
                .thenReturn(Optional.of(easyRandom.nextObject(DiscordWebhookConfig.class)));
        doThrow(new RuntimeException("Simulated failure"))
                .when(discordWebhookClient)
                .send(anyString(), any(DiscordWebhookPayload.class));

        notificationService.notify(notification);

        int recipients = notification.getRecipients().size();
        verify(getDiscordWebhookByUserIdPort, times(recipients)).getByUserId(anyString());
        verify(discordWebhookClient, times(recipients)).send(anyString(), any(DiscordWebhookPayload.class));
    }
}
