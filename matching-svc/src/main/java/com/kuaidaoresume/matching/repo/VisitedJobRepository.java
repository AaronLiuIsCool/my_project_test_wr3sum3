package com.kuaidaoresume.matching.repo;

import com.kuaidaoresume.matching.model.VisitedJob;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface VisitedJobRepository extends MongoRepository<VisitedJob, String>, EnhancedVisitedJobRepository {
    Optional<VisitedJob> findByJobUuid(String jobUuid);
}
