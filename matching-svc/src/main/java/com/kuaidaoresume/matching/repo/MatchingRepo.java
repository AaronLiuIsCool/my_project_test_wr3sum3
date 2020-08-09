package com.kuaidaoresume.matching.repo;

import com.kuaidaoresume.matching.model.Matching;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MatchingRepo extends MongoRepository<Matching, Long> {
    //TODO: after DB schema finalized
}
