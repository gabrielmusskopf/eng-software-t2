package br.com.unisinos.es.t2.application.domain.service.discordwebhook;

import br.com.unisinos.es.t2.application.domain.exception.ClientException;
import br.com.unisinos.es.t2.application.domain.exception.NotAuthenticatedException;
import br.com.unisinos.es.t2.application.domain.model.DiscordWebhookConfig;
import br.com.unisinos.es.t2.application.domain.model.User;
import br.com.unisinos.es.t2.application.port.in.auth.GetAuthenticatedUserPort;
import br.com.unisinos.es.t2.application.port.in.discordwebhook.RegisterDiscordWebhookService;
import br.com.unisinos.es.t2.application.port.out.discordwebhook.GetDiscordWebhookByUserIdPort;
import br.com.unisinos.es.t2.application.port.out.discordwebhook.SaveDiscordWebhookPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class RegisterDiscordWebhookImpl implements RegisterDiscordWebhookService {

    private final GetAuthenticatedUserPort getAuthenticatedUserPort;
    private final DiscordWebhookConfigMapper mapper;
    private final GetDiscordWebhookByUserIdPort getDiscordWebhookByUserIdPort;
    private final SaveDiscordWebhookPort saveDiscordWebhookPort;

    @Override
    public DiscordWebhookConfig register(RegisterDiscordWebhookCommand command) {
        if (command.webhookUrl() == null || command.webhookUrl().isBlank()) {
            throw new ClientException("webhookUrl is required");
        }

        User user = getAuthenticatedUserPort.getAuthenticatedUser().orElseThrow(NotAuthenticatedException::new);

        DiscordWebhookConfig config = getDiscordWebhookByUserIdPort
                .getByUserId(user.getId())
                .map(existing -> {
                    existing.setWebhookUrl(command.webhookUrl());
                    return existing;
                })
                .orElseGet(() -> mapper.toConfig(command.webhookUrl(), user));

        log.debug("Registering Discord webhook for user {}", user.getId());
        return saveDiscordWebhookPort.save(config);
    }
}
