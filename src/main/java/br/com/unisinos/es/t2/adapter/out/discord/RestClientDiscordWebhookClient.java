package br.com.unisinos.es.t2.adapter.out.discord;

import br.com.unisinos.es.t2.application.domain.model.DiscordWebhookPayload;
import br.com.unisinos.es.t2.application.port.out.discordwebhook.DiscordWebhookClient;
import br.com.unisinos.es.t2.application.port.out.discordwebhook.DiscordWebhookProperties;
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
        factory.setConnectTimeout(properties.getConnectTimeout());
        factory.setReadTimeout(properties.getReadTimeout());
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
