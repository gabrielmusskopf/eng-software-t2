package br.com.unisinos.es.t2.adapter.out.discord;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
record DiscordWebhookPayload(String content, String username, List<Embed> embeds) {

    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    record Embed(String title, String description, Integer color, List<Field> fields) {}

    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    record Field(String name, String value, Boolean inline) {}
}
