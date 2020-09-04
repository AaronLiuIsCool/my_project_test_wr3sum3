package com.kuaidaoresume.matching.repo;

import com.kuaidaoresume.matching.model.Resume;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ResumeRepository extends MongoRepository<Resume, String>, EnhancedResumeRepository {
    Optional<Resume> findByResumeUuid(String resumeUuid);
}
