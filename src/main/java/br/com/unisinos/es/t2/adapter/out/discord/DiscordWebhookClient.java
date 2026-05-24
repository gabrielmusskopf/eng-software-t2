package br.com.unisinos.es.t2.adapter.out.discord;

interface DiscordWebhookClient {

    void send(String webhookUrl, DiscordWebhookPayload payload);
}
