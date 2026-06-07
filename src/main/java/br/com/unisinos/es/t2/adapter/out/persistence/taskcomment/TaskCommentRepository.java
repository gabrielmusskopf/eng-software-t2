package br.com.unisinos.es.t2.adapter.out.persistence.taskcomment;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskCommentRepository extends MongoRepository<TaskCommentEntity, String> {

    List<TaskCommentEntity> findByTaskId(String taskId);
}
