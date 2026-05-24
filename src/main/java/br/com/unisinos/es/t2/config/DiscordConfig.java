package br.com.unisinos.es.t2.config;

import br.com.unisinos.es.t2.adapter.out.discord.DiscordWebhookProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(DiscordWebhookProperties.class)
class DiscordConfig {}
