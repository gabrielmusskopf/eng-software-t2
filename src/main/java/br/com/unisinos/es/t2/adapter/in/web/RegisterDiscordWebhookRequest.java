package br.com.unisinos.es.t2.adapter.in.web;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDiscordWebhookRequest {

    @NotEmpty
    @URL
    private String webhookUrl;

    private String username;
}
