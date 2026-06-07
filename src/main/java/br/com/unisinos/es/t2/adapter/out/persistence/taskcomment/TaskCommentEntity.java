package br.com.unisinos.es.t2.adapter.out.persistence.taskcomment;

import br.com.unisinos.es.t2.adapter.out.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "task_comments")
@EqualsAndHashCode(callSuper = true)
public class TaskCommentEntity extends Entity {
    private String taskId;
    private String userId;
    private String content;
}
