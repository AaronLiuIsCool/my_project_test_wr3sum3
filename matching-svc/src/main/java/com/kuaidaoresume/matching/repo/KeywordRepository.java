package com.kuaidaoresume.matching.repo;

import com.kuaidaoresume.matching.model.Keyword;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface KeywordRepository extends MongoRepository<Keyword, String> {
}
