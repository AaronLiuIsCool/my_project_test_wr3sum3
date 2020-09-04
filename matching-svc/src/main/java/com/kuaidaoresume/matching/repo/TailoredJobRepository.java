package com.kuaidaoresume.matching.repo;

import com.kuaidaoresume.matching.model.TailoredJob;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TailoredJobRepository extends MongoRepository<TailoredJob, String>, EnhancedTailoredJobRepository {
    Optional<TailoredJob> findByJobUuid(String jobUuid);
}
