package br.com.unisinos.es.t2.adapter.out.persistence.task;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

public interface TaskRepository extends MongoRepository<TaskEntity, String> {

    Optional<TaskEntity> findByIdAndDeletedFalse(String id);

    @Query("{ '_id': ?0, 'deleted': false }")
    @Update("{ '$set': { 'deleted': true} }")
    void deleteById(String id);
}
