package br.com.unisinos.es.t2.adapter.out.discord;

import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
class RestClientDiscordWebhookClient implements DiscordWebhookClient {

    private final RestClient restClient;

    RestClientDiscordWebhookClient(DiscordWebhookProperties properties) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofMillis(properties.connectTimeoutMs()));
        factory.setReadTimeout(Duration.ofMillis(properties.readTimeoutMs()));
        this.restClient = RestClient.builder().requestFactory(factory).build();
    }

    @Override
    public void send(String webhookUrl, DiscordWebhookPayload payload) {
        restClient
                .post()
                .uri(webhookUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .retrieve()
                .toBodilessEntity();
        log.debug("Discord webhook delivered to {}", maskUrl(webhookUrl));
    }

    private static String maskUrl(String url) {
        if (url == null || url.length() < 12) {
            return "***";
        }
        return url.substring(0, 8) + "..." + url.substring(url.length() - 4);
    }
}
