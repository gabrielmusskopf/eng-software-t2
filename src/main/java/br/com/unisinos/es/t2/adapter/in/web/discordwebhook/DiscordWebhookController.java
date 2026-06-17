package br.com.unisinos.es.t2.adapter.in.web.discordwebhook;

import br.com.unisinos.es.t2.adapter.in.web.ApiResponse;
import br.com.unisinos.es.t2.application.domain.model.DiscordWebhookConfig;
import br.com.unisinos.es.t2.application.port.in.discordwebhook.DeleteDiscordWebhookService;
import br.com.unisinos.es.t2.application.port.in.discordwebhook.GetDiscordWebhookService;
import br.com.unisinos.es.t2.application.port.in.discordwebhook.RegisterDiscordWebhookService;
import br.com.unisinos.es.t2.application.port.in.discordwebhook.RegisterDiscordWebhookService.RegisterDiscordWebhookCommand;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Discord Webhooks", description = "Configuração das notificações do Discord")
@RestController
@RequestMapping("/webhooks/discord")
@RequiredArgsConstructor
class DiscordWebhookController {

    private final DiscordWebhookMapper mapper;
    private final RegisterDiscordWebhookService registerService;
    private final GetDiscordWebhookService getService;
    private final DeleteDiscordWebhookService deleteService;

    @PutMapping
    public ResponseEntity<ApiResponse<DiscordWebhookResponse>> register(
            @Valid @RequestBody RegisterDiscordWebhookRequest request) {
        RegisterDiscordWebhookCommand command = new RegisterDiscordWebhookCommand(request.getWebhookUrl());
        DiscordWebhookConfig saved = registerService.register(command);
        return ApiResponse.success(200, "Discord webhook registered", mapper.toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<DiscordWebhookResponse>> get() {
        DiscordWebhookConfig config = getService.get();
        return ApiResponse.success(200, "Discord webhook retrieved", mapper.toResponse(config));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> delete() {
        deleteService.delete();
        return ApiResponse.success(200, "Discord webhook deleted");
    }
}
