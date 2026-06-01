package br.com.unisinos.es.t2.adapter.in.web.discordwebhook;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
class RegisterDiscordWebhookRequest {

    @URL
    @NotEmpty
    private String webhookUrl;
}
