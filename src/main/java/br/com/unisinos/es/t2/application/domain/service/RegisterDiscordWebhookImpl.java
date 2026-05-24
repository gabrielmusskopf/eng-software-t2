package br.com.unisinos.es.t2.application.domain.service;

import br.com.unisinos.es.t2.application.domain.exception.ClientException;
import br.com.unisinos.es.t2.application.domain.exception.NotFoundException;
import br.com.unisinos.es.t2.application.domain.model.DiscordWebhookConfig;
import br.com.unisinos.es.t2.application.port.in.RegisterDiscordWebhookService;
import br.com.unisinos.es.t2.application.port.out.ExistsUserByIdPort;
import br.com.unisinos.es.t2.application.port.out.GetDiscordWebhookByUserIdPort;
import br.com.unisinos.es.t2.application.port.out.SaveDiscordWebhookPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class RegisterDiscordWebhookImpl implements RegisterDiscordWebhookService {

    private final DiscordWebhookConfigMapper mapper;
    private final ExistsUserByIdPort existsUserByIdPort;
    private final GetDiscordWebhookByUserIdPort getDiscordWebhookByUserIdPort;
    private final SaveDiscordWebhookPort saveDiscordWebhookPort;

    @Override
    public DiscordWebhookConfig register(RegisterDiscordWebhookCommand command) {
        if (command.webhookUrl() == null || command.webhookUrl().isBlank()) {
            throw new ClientException("webhookUrl is required");
        }
        if (!existsUserByIdPort.exists(command.userId())) {
            throw new NotFoundException("User not found");
        }

        DiscordWebhookConfig config = getDiscordWebhookByUserIdPort
                .getByUserId(command.userId())
                .map(existing -> {
                    existing.setWebhookUrl(command.webhookUrl());
                    existing.setUsername(command.username());
                    return existing;
                })
                .orElseGet(() -> mapper.toConfig(command));

        log.debug("Registering Discord webhook for user {}", command.userId());
        return saveDiscordWebhookPort.save(config);
    }
}
