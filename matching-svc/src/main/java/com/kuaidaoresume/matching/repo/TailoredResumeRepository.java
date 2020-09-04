package com.kuaidaoresume.matching.repo;

import com.kuaidaoresume.matching.model.TailoredResume;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TailoredResumeRepository extends MongoRepository<TailoredResume, String> {
    Optional<TailoredResume> findByResumeUuid(String resumeUuid);
}
