package br.com.unisinos.es.t2.adapter.out.discord;

import br.com.unisinos.es.t2.application.port.out.discordwebhook.DiscordWebhookProperties;
import java.time.Duration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.notifications.discord")
class SpringDiscordWebhookProperties implements DiscordWebhookProperties {
    private boolean enabled;
    private String defaultUsername = "Task Manager";
    private Duration connectTimeout = Duration.ofSeconds(5);
    private Duration readTimeout = Duration.ofSeconds(5);
    private Events events = new Events();

    @Data
    public static class Events implements DiscordWebhookProperties.Events {
        private Event taskCreated = new Event();
    }

    @Data
    public static class Event implements DiscordWebhookProperties.Event {
        private boolean enabled;
    }
}
