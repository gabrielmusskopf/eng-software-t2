package br.com.unisinos.es.t2.application.domain.model;

import java.util.Set;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DiscordNotification {

    private Set<String> recipients;
    private DiscordWebhookPayload payload;
}
