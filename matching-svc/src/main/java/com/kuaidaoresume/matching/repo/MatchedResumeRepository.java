package com.kuaidaoresume.matching.repo;

import com.kuaidaoresume.matching.model.MatchedResume;
import org.springframework.data.mongodb.repository.MongoRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface MatchedResumeRepository extends MongoRepository<MatchedResume, String> {
    Optional<MatchedResume> findByResumeUuid(String resumeUuid);
}
