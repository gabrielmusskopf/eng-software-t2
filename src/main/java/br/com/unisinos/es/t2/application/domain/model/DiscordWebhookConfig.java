package br.com.unisinos.es.t2.application.domain.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DiscordWebhookConfig {
    private String id;
    private String userId;
    private String webhookUrl;
    private String username;
    private boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
