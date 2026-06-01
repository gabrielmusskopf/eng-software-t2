package br.com.unisinos.es.t2.application.port.out.discordwebhook;

import java.time.Duration;

public interface DiscordWebhookProperties {

    boolean isEnabled();

    String getDefaultUsername();

    Duration getConnectTimeout();

    Duration getReadTimeout();

    Events getEvents();

    interface Events {
        Event getTaskCreated();
    }

    interface Event {
        boolean isEnabled();
    }
}
