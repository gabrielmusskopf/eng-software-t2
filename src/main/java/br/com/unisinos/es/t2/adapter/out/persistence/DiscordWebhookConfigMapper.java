package br.com.unisinos.es.t2.adapter.out.persistence;

import br.com.unisinos.es.t2.application.domain.model.DiscordWebhookConfig;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface DiscordWebhookConfigMapper {

    DiscordWebhookConfigEntity toEntity(DiscordWebhookConfig config);

    DiscordWebhookConfig toDomain(DiscordWebhookConfigEntity entity);
}
