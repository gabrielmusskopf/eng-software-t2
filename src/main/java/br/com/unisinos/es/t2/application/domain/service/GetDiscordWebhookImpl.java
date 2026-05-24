package br.com.unisinos.es.t2.application.domain.service;

import br.com.unisinos.es.t2.application.domain.exception.NotFoundException;
import br.com.unisinos.es.t2.application.domain.model.DiscordWebhookConfig;
import br.com.unisinos.es.t2.application.port.in.GetDiscordWebhookService;
import br.com.unisinos.es.t2.application.port.out.GetDiscordWebhookByUserIdPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class GetDiscordWebhookImpl implements GetDiscordWebhookService {

    private final GetDiscordWebhookByUserIdPort getDiscordWebhookByUserIdPort;

    @Override
    public DiscordWebhookConfig get(GetDiscordWebhookCommand command) {
        return getDiscordWebhookByUserIdPort
                .getByUserId(command.userId())
                .orElseThrow(() -> new NotFoundException("Discord webhook not configured for user"));
    }
}
