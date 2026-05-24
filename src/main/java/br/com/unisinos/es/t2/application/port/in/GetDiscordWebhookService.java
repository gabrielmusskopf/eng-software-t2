package br.com.unisinos.es.t2.application.port.in;

import br.com.unisinos.es.t2.application.domain.model.DiscordWebhookConfig;

public interface GetDiscordWebhookService {

    DiscordWebhookConfig get(GetDiscordWebhookCommand command);

    record GetDiscordWebhookCommand(String userId) {}
}
