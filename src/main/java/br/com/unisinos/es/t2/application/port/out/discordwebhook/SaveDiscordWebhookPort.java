package br.com.unisinos.es.t2.application.port.out.discordwebhook;

import br.com.unisinos.es.t2.application.domain.model.DiscordWebhookConfig;

public interface SaveDiscordWebhookPort {
    DiscordWebhookConfig save(DiscordWebhookConfig config);
}
