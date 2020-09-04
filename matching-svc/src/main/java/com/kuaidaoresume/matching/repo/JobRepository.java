package com.kuaidaoresume.matching.repo;

import com.kuaidaoresume.matching.model.Job;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface JobRepository extends MongoRepository<Job, String>, EnhancedJobRepository {
    Optional<Job> findTopByOrderByCreatedAtDesc();

    Optional<Job> findByJobUuid(String jobUuid);
}
