package br.com.unisinos.es.t2.adapter.out.persistence;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import jakarta.annotation.Nonnull;

interface UserRepository extends MongoRepository<UserEntity, String> {

	Optional<UserEntity> findByIdAndDeletedFalse(String id);

    Optional<UserEntity> findByEmailAndDeletedFalse(String email);

    @Query("{ 'id': ?0 }")
    @Update("{ $set: { 'deleted': true } }")
    void deleteById(@Nonnull String id);

	boolean existsByIdAndDeletedFalse(String id);

    boolean existsByEmailAndDeletedFalse(String email);
}
