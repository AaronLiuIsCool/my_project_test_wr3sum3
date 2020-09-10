package com.kuaidaoresume.matching.repo;

import com.kuaidaoresume.matching.model.BookmarkedResume;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BookmarkedResumeRepository extends MongoRepository<BookmarkedResume, String>, EnhancedBookmarkedResumeRepository {
    Optional<BookmarkedResume> findByResumeUuid(String resumeUuid);
}
