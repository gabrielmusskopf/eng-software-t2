package br.com.unisinos.es.t2.adapter.out.persistence.discordwebhook;

import jakarta.annotation.Nonnull;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

interface DiscordWebhookConfigRepository extends MongoRepository<DiscordWebhookConfigEntity, String> {

    Optional<DiscordWebhookConfigEntity> findByUserIdAndDeletedFalse(String userId);

    @Query("{ 'userId': ?0 }")
    @Update("{ $set: { 'deleted': true } }")
    void deleteByUserId(@Nonnull String userId);
}
