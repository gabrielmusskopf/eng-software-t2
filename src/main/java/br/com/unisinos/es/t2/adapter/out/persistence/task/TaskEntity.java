package br.com.unisinos.es.t2.adapter.out.persistence.task;

import br.com.unisinos.es.t2.adapter.out.persistence.Entity;
import br.com.unisinos.es.t2.application.domain.model.TaskStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tasks")
@EqualsAndHashCode(callSuper = true)
public class TaskEntity extends Entity {
    private String title;
    private String description;
    private String userId;
    private TaskStatus status;
    private LocalDateTime statusUpdatedAt;
}
