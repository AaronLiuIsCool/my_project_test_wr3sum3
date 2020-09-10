package com.kuaidaoresume.matching.repo;

import com.kuaidaoresume.matching.model.BookmarkedResume;
import com.kuaidaoresume.matching.model.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.ArrayList;
import java.util.List;

public class EnhancedBookmarkedResumeRepositoryImpl implements EnhancedBookmarkedResumeRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Job> findBookmarkedJobs(String resumeUuid, int offset, int limit) {
        TypedAggregation<BookmarkedResume> aggregation = new TypedAggregation<>(
            BookmarkedResume.class,
            Aggregation.match(Criteria.where("resumeUuid").is(resumeUuid)),
            Aggregation.project("bookmarkedJobs").and("bookmarkedJobs").slice(limit, offset)
        );
        BookmarkedResume bookmarkedResume = mongoTemplate.aggregate(aggregation, BookmarkedResume.class, BookmarkedResume.class)
            .getUniqueMappedResult();
        return new ArrayList<>(bookmarkedResume.getBookmarkedJobs());
    }
}
