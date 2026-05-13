package br.com.unisinos.es.t2.application.domain.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private String id;
    private String name;
    private String password;
    private String email;
	private boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
