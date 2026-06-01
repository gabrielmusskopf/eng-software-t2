package br.com.unisinos.es.t2.application.port.out.discordwebhook;

import br.com.unisinos.es.t2.application.domain.model.DiscordWebhookPayload;

public interface DiscordWebhookClient {

    void send(String webhookUrl, DiscordWebhookPayload payload);
}
