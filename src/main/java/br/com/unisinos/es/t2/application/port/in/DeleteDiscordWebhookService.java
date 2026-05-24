package br.com.unisinos.es.t2.application.port.in;

public interface DeleteDiscordWebhookService {

    void delete(DeleteDiscordWebhookCommand command);

    record DeleteDiscordWebhookCommand(String userId) {}
}
