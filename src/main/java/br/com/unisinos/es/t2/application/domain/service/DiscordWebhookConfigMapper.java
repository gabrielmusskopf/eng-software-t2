package br.com.unisinos.es.t2.application.domain.service;

import br.com.unisinos.es.t2.application.domain.model.DiscordWebhookConfig;
import br.com.unisinos.es.t2.application.port.in.RegisterDiscordWebhookService.RegisterDiscordWebhookCommand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface DiscordWebhookConfigMapper {

    DiscordWebhookConfig toConfig(RegisterDiscordWebhookCommand command);
}
