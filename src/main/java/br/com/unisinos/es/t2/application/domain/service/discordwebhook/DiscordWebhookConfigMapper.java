package br.com.unisinos.es.t2.application.domain.service.discordwebhook;

import br.com.unisinos.es.t2.application.domain.model.DiscordWebhookConfig;
import br.com.unisinos.es.t2.application.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
interface DiscordWebhookConfigMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "username", source = "user.name")
    DiscordWebhookConfig toConfig(String webhookUrl, User user);
}
