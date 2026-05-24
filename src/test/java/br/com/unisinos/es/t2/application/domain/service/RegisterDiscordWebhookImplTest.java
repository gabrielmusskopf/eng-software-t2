package br.com.unisinos.es.t2.application.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.unisinos.es.t2.application.domain.exception.ClientException;
import br.com.unisinos.es.t2.application.domain.exception.NotFoundException;
import br.com.unisinos.es.t2.application.domain.model.DiscordWebhookConfig;
import br.com.unisinos.es.t2.application.port.in.RegisterDiscordWebhookService.RegisterDiscordWebhookCommand;
import br.com.unisinos.es.t2.application.port.out.ExistsUserByIdPort;
import br.com.unisinos.es.t2.application.port.out.GetDiscordWebhookByUserIdPort;
import br.com.unisinos.es.t2.application.port.out.SaveDiscordWebhookPort;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RegisterDiscordWebhookImplTest {

    @Mock
    private DiscordWebhookConfigMapper mapper;

    @Mock
    private ExistsUserByIdPort existsUserByIdPort;

    @Mock
    private GetDiscordWebhookByUserIdPort getDiscordWebhookByUserIdPort;

    @Mock
    private SaveDiscordWebhookPort saveDiscordWebhookPort;

    @InjectMocks
    private RegisterDiscordWebhookImpl service;

    @Test
    void register_newConfig_savesNew() {
        RegisterDiscordWebhookCommand command =
                new RegisterDiscordWebhookCommand("user-1", "https://discord.com/api/webhooks/abc/xyz", "Bot");
        DiscordWebhookConfig fromMapper = DiscordWebhookConfig.builder()
                .userId("user-1")
                .webhookUrl("https://discord.com/api/webhooks/abc/xyz")
                .username("Bot")
                .build();
        when(existsUserByIdPort.exists("user-1")).thenReturn(true);
        when(getDiscordWebhookByUserIdPort.getByUserId("user-1")).thenReturn(Optional.empty());
        when(mapper.toConfig(command)).thenReturn(fromMapper);
        when(saveDiscordWebhookPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        DiscordWebhookConfig result = service.register(command);

        ArgumentCaptor<DiscordWebhookConfig> captor = ArgumentCaptor.forClass(DiscordWebhookConfig.class);
        verify(saveDiscordWebhookPort).save(captor.capture());
        assertThat(captor.getValue().getUserId()).isEqualTo("user-1");
        assertThat(captor.getValue().getWebhookUrl()).isEqualTo("https://discord.com/api/webhooks/abc/xyz");
        assertThat(result).isSameAs(captor.getValue());
    }

    @Test
    void register_existingConfig_updatesUrlAndUsername() {
        RegisterDiscordWebhookCommand command = new RegisterDiscordWebhookCommand("user-1", "https://new", "NewName");
        DiscordWebhookConfig existing = DiscordWebhookConfig.builder()
                .id("cfg-1")
                .userId("user-1")
                .webhookUrl("https://old")
                .username("OldName")
                .build();
        when(existsUserByIdPort.exists("user-1")).thenReturn(true);
        when(getDiscordWebhookByUserIdPort.getByUserId("user-1")).thenReturn(Optional.of(existing));
        when(saveDiscordWebhookPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        service.register(command);

        assertThat(existing.getWebhookUrl()).isEqualTo("https://new");
        assertThat(existing.getUsername()).isEqualTo("NewName");
        verify(saveDiscordWebhookPort).save(existing);
    }

    @Test
    void register_blankUrl_throwsClientException() {
        RegisterDiscordWebhookCommand command = new RegisterDiscordWebhookCommand("user-1", "  ", null);

        assertThatThrownBy(() -> service.register(command)).isInstanceOf(ClientException.class);
        verify(saveDiscordWebhookPort, never()).save(any());
    }

    @Test
    void register_userNotFound_throwsNotFound() {
        RegisterDiscordWebhookCommand command = new RegisterDiscordWebhookCommand("ghost", "https://x", null);
        when(existsUserByIdPort.exists("ghost")).thenReturn(false);

        assertThatThrownBy(() -> service.register(command)).isInstanceOf(NotFoundException.class);
        verify(saveDiscordWebhookPort, never()).save(any());
    }
}
