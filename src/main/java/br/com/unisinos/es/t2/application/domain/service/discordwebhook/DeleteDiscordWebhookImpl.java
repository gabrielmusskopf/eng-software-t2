package br.com.unisinos.es.t2.application.domain.service.discordwebhook;

import br.com.unisinos.es.t2.application.domain.exception.NotAuthenticatedException;
import br.com.unisinos.es.t2.application.domain.exception.NotFoundException;
import br.com.unisinos.es.t2.application.domain.model.User;
import br.com.unisinos.es.t2.application.port.in.auth.GetAuthenticatedUserPort;
import br.com.unisinos.es.t2.application.port.in.discordwebhook.DeleteDiscordWebhookService;
import br.com.unisinos.es.t2.application.port.out.discordwebhook.DeleteDiscordWebhookPort;
import br.com.unisinos.es.t2.application.port.out.discordwebhook.GetDiscordWebhookByUserIdPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class DeleteDiscordWebhookImpl implements DeleteDiscordWebhookService {

    private final GetAuthenticatedUserPort getAuthenticatedUserPort;
    private final GetDiscordWebhookByUserIdPort getDiscordWebhookByUserIdPort;
    private final DeleteDiscordWebhookPort deleteDiscordWebhookPort;

    @Override
    public void delete() {
        User user = getAuthenticatedUserPort.getAuthenticatedUser().orElseThrow(NotAuthenticatedException::new);

        if (getDiscordWebhookByUserIdPort.getByUserId(user.getId()).isEmpty()) {
            throw new NotFoundException("Discord webhook not configured for user");
        }
        deleteDiscordWebhookPort.deleteByUserId(user.getId());
    }
}
