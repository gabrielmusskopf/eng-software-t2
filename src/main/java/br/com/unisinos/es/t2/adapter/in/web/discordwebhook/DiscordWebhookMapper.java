package br.com.unisinos.es.t2.adapter.in.web.discordwebhook;

import br.com.unisinos.es.t2.application.domain.model.DiscordWebhookConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
interface DiscordWebhookMapper {

    @Mapping(target = "webhookUrl", expression = "java(maskUrl(config.getWebhookUrl()))")
    DiscordWebhookResponse toResponse(DiscordWebhookConfig config);

    default String maskUrl(String url) {
        if (url == null) {
            return null;
        }
        if (url.length() <= 12) {
            return "***";
        }
        return url.substring(0, 8) + "..." + url.substring(url.length() - 4);
    }
}
