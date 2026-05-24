package br.com.unisinos.es.t2.adapter.out.discord;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.discord")
public record DiscordWebhookProperties(
        boolean enabled, String defaultUsername, int connectTimeoutMs, int readTimeoutMs) {

    public DiscordWebhookProperties {
        if (defaultUsername == null || defaultUsername.isBlank()) {
            defaultUsername = "Task Manager";
        }
        if (connectTimeoutMs <= 0) {
            connectTimeoutMs = 3000;
        }
        if (readTimeoutMs <= 0) {
            readTimeoutMs = 5000;
        }
    }
}
