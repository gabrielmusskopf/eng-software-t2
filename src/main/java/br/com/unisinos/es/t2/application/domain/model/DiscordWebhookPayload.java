package br.com.unisinos.es.t2.application.domain.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DiscordWebhookPayload {
    private String content;
    private String username;
    private List<Embed> embeds;

    @Data
    @Builder
    public static class Embed {
        private String title;
        private String description;
        private Integer color;
        private List<Field> fields;
    }

    @Data
    @Builder
    public static class Field {
        private String name;
        private String value;
        private Boolean inline;
    }

    public static class EmbedFieldsBuilder {
        private List<Field> fields = new ArrayList<>();

        public EmbedFieldsBuilder addField(String name, String value) {
            return addField(name, value, false);
        }

        public EmbedFieldsBuilder addFieldInline(String name, String value) {
            return addField(name, value, true);
        }

        private EmbedFieldsBuilder addField(String name, String value, boolean inline) {
            if (value == null || value.isBlank()) {
                return this;
            }
            if (fields == null) {
                fields = new ArrayList<>();
            }
            fields.add(new Field(name, value, inline));
            return this;
        }

        public List<Field> build() {
            return fields;
        }
    }
}
