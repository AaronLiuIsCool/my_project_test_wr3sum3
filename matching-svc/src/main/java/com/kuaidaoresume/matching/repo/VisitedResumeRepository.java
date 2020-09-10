package com.kuaidaoresume.matching.repo;

import com.kuaidaoresume.matching.model.VisitedResume;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface VisitedResumeRepository extends MongoRepository<VisitedResume, String>, EnhancedVisitedResumeRepository {
    Optional<VisitedResume> findByResumeUuid(String resumeUuid);
}
