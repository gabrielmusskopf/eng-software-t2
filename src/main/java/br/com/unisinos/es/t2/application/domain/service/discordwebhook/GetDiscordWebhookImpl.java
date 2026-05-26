package br.com.unisinos.es.t2.application.domain.service.discordwebhook;

import br.com.unisinos.es.t2.application.domain.exception.NotAuthenticatedException;
import br.com.unisinos.es.t2.application.domain.exception.NotFoundException;
import br.com.unisinos.es.t2.application.domain.model.DiscordWebhookConfig;
import br.com.unisinos.es.t2.application.domain.model.User;
import br.com.unisinos.es.t2.application.port.in.auth.GetAuthenticatedUserPort;
import br.com.unisinos.es.t2.application.port.in.discordwebhook.GetDiscordWebhookService;
import br.com.unisinos.es.t2.application.port.out.discordwebhook.GetDiscordWebhookByUserIdPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class GetDiscordWebhookImpl implements GetDiscordWebhookService {

    private final GetAuthenticatedUserPort getAuthenticatedUserPort;
    private final GetDiscordWebhookByUserIdPort getDiscordWebhookByUserIdPort;

    @Override
    public DiscordWebhookConfig get() {
        User user = getAuthenticatedUserPort.getAuthenticatedUser().orElseThrow(NotAuthenticatedException::new);

        return getDiscordWebhookByUserIdPort
                .getByUserId(user.getId())
                .orElseThrow(() -> new NotFoundException("Discord webhook not configured for user"));
    }
}
