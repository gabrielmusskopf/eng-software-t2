package br.com.unisinos.es.t2.application.port.in.discordwebhook;

import br.com.unisinos.es.t2.application.domain.model.DiscordWebhookConfig;

public interface GetDiscordWebhookService {

    DiscordWebhookConfig get();
}
