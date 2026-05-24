package br.com.unisinos.es.t2.adapter.out.persistence;

import jakarta.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

interface TaskRepository extends MongoRepository<TaskEntity, String> {

    Optional<TaskEntity> findByIdAndDeletedFalse(String id);

    List<TaskEntity> findAllByAssigneeIdAndDeletedFalse(String assigneeId);

    boolean existsByIdAndDeletedFalse(String id);

    @Query("{ 'id': ?0 }")
    @Update("{ $set: { 'deleted': true } }")
    void deleteById(@Nonnull String id);
}
