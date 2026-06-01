package br.com.unisinos.es.t2.adapter.out.persistence.discordwebhook;

import br.com.unisinos.es.t2.adapter.out.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "discord_webhook_configs")
@EqualsAndHashCode(callSuper = true)
class DiscordWebhookConfigEntity extends Entity {

    @Indexed(unique = true)
    private String userId;

    private String webhookUrl;
    private String username;
}
