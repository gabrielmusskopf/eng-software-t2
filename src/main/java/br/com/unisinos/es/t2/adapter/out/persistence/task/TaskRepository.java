package br.com.unisinos.es.t2.adapter.out.persistence.task;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<TaskEntity, String> {}
