package br.com.unisinos.es.t2.adapter.in.web;

import lombok.Data;

@Data
public class DiscordWebhookResponse {
    private String userId;
    private String webhookUrl;
    private String username;
}
