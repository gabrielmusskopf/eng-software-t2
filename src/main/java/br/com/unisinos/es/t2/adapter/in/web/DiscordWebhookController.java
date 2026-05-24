package br.com.unisinos.es.t2.adapter.in.web;

import br.com.unisinos.es.t2.application.domain.model.DiscordWebhookConfig;
import br.com.unisinos.es.t2.application.port.in.DeleteDiscordWebhookService;
import br.com.unisinos.es.t2.application.port.in.DeleteDiscordWebhookService.DeleteDiscordWebhookCommand;
import br.com.unisinos.es.t2.application.port.in.GetDiscordWebhookService;
import br.com.unisinos.es.t2.application.port.in.GetDiscordWebhookService.GetDiscordWebhookCommand;
import br.com.unisinos.es.t2.application.port.in.RegisterDiscordWebhookService;
import br.com.unisinos.es.t2.application.port.in.RegisterDiscordWebhookService.RegisterDiscordWebhookCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/{userId}/webhooks/discord")
@RequiredArgsConstructor
class DiscordWebhookController {

    private final DiscordWebhookMapper mapper;
    private final RegisterDiscordWebhookService registerService;
    private final GetDiscordWebhookService getService;
    private final DeleteDiscordWebhookService deleteService;

    @PutMapping
    public ResponseEntity<ApiResponse<DiscordWebhookResponse>> register(
            @PathVariable String userId, @Valid @RequestBody RegisterDiscordWebhookRequest request) {
        DiscordWebhookConfig saved = registerService.register(
                new RegisterDiscordWebhookCommand(userId, request.getWebhookUrl(), request.getUsername()));
        return ApiResponse.success(200, "Discord webhook registered", mapper.toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<DiscordWebhookResponse>> get(@PathVariable String userId) {
        DiscordWebhookConfig config = getService.get(new GetDiscordWebhookCommand(userId));
        return ApiResponse.success(200, "Discord webhook retrieved", mapper.toResponse(config));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String userId) {
        deleteService.delete(new DeleteDiscordWebhookCommand(userId));
        return ApiResponse.success(200, "Discord webhook deleted");
    }
}
