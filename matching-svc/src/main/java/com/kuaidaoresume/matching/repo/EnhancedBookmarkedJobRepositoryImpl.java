package com.kuaidaoresume.matching.repo;

import com.google.common.collect.Lists;
import com.kuaidaoresume.matching.model.BookmarkedJob;
import com.kuaidaoresume.matching.model.Resume;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EnhancedBookmarkedJobRepositoryImpl implements EnhancedBookmarkedJobRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Resume> findResumesBookmarkedByJob(String jobUuid, int offset, int limit) {
        TypedAggregation<BookmarkedJob> aggregation = new TypedAggregation<>(
            BookmarkedJob.class,
            Aggregation.match(Criteria.where("jobUuid").is(jobUuid)),
            Aggregation.project("bookmarkedResumes").and("bookmarkedResumes").slice(limit, offset)
        );
        BookmarkedJob bookmarkedJob = mongoTemplate.aggregate(aggregation, BookmarkedJob.class, BookmarkedJob.class)
            .getUniqueMappedResult();
        return Objects.nonNull(bookmarkedJob) ? new ArrayList<>(bookmarkedJob.getBookmarkedResumes()) :
            Lists.newArrayList();
    }
}
