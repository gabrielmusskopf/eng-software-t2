package br.com.unisinos.es.t2.application.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskStatus {
    BACKLOG("Backlog"),
    IN_PROGRESS("Em progresso"),
    COMPLETED("Concluída"),
    CANCELLED("Cancelada");

    private final String displayName;
}
