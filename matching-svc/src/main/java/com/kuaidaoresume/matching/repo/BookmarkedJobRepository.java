package com.kuaidaoresume.matching.repo;

import com.kuaidaoresume.matching.model.BookmarkedJob;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BookmarkedJobRepository extends MongoRepository<BookmarkedJob, String>, EnhancedBookmarkedJobRepository {
    Optional<BookmarkedJob> findByJobUuid(String jobUuid);
}
