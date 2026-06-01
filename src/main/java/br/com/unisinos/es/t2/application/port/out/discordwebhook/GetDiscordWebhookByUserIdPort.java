package br.com.unisinos.es.t2.application.port.out.discordwebhook;

import br.com.unisinos.es.t2.application.domain.model.DiscordWebhookConfig;
import java.util.Optional;

public interface GetDiscordWebhookByUserIdPort {

    Optional<DiscordWebhookConfig> getByUserId(String userId);
}
