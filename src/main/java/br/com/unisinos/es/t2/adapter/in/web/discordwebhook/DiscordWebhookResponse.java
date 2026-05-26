package br.com.unisinos.es.t2.adapter.in.web.discordwebhook;

import lombok.Data;

@Data
class DiscordWebhookResponse {
    private String userId;
    private String webhookUrl;
    private String username;
}
