package br.com.unisinos.es.t2.application.domain.service;

import br.com.unisinos.es.t2.application.domain.exception.NotFoundException;
import br.com.unisinos.es.t2.application.port.in.DeleteDiscordWebhookService;
import br.com.unisinos.es.t2.application.port.out.DeleteDiscordWebhookPort;
import br.com.unisinos.es.t2.application.port.out.GetDiscordWebhookByUserIdPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class DeleteDiscordWebhookImpl implements DeleteDiscordWebhookService {

    private final GetDiscordWebhookByUserIdPort getDiscordWebhookByUserIdPort;
    private final DeleteDiscordWebhookPort deleteDiscordWebhookPort;

    @Override
    public void delete(DeleteDiscordWebhookCommand command) {
        if (getDiscordWebhookByUserIdPort.getByUserId(command.userId()).isEmpty()) {
            throw new NotFoundException("Discord webhook not configured for user");
        }
        deleteDiscordWebhookPort.deleteByUserId(command.userId());
    }
}
