package br.com.unisinos.es.t2.adapter.out.persistence;

import br.com.unisinos.es.t2.application.domain.model.DiscordWebhookConfig;
import br.com.unisinos.es.t2.application.port.out.DeleteDiscordWebhookPort;
import br.com.unisinos.es.t2.application.port.out.GetDiscordWebhookByUserIdPort;
import br.com.unisinos.es.t2.application.port.out.SaveDiscordWebhookPort;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class DiscordWebhookConfigPersistenceAdapter
        implements SaveDiscordWebhookPort, GetDiscordWebhookByUserIdPort, DeleteDiscordWebhookPort {

    private final DiscordWebhookConfigMapper mapper;
    private final DiscordWebhookConfigRepository repository;

    @Override
    public DiscordWebhookConfig save(DiscordWebhookConfig config) {
        DiscordWebhookConfigEntity saved = repository.save(mapper.toEntity(config));
        config.setId(saved.getId());
        config.setCreatedAt(saved.getCreatedAt());
        config.setUpdatedAt(saved.getUpdatedAt());
        return config;
    }

    @Override
    public Optional<DiscordWebhookConfig> getByUserId(String userId) {
        return repository.findByUserIdAndDeletedFalse(userId).map(mapper::toDomain);
    }

    @Override
    public void deleteByUserId(String userId) {
        repository.deleteByUserId(userId);
    }
}
