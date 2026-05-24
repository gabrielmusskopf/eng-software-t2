package br.com.unisinos.es.t2.application.port.in;

import br.com.unisinos.es.t2.application.domain.model.DiscordWebhookConfig;

public interface RegisterDiscordWebhookService {

    DiscordWebhookConfig register(RegisterDiscordWebhookCommand command);

    record RegisterDiscordWebhookCommand(String userId, String webhookUrl, String username) {}
}
