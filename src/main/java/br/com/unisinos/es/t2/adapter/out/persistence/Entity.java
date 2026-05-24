package br.com.unisinos.es.t2.adapter.out.persistence;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

@Data
public class Entity {

    @Id
    @NotBlank
    protected String id;

    @NotNull
    @CreatedDate
    protected LocalDateTime createdAt;

    @NotNull
    @LastModifiedDate
    protected LocalDateTime updatedAt;

    @NotNull
    @Version
    protected Long version;

    protected boolean deleted;
}
