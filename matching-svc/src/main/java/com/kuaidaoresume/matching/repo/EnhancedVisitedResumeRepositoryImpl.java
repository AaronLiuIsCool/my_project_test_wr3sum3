package com.kuaidaoresume.matching.repo;

import com.kuaidaoresume.matching.model.Job;
import com.kuaidaoresume.matching.model.VisitedResume;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.ArrayList;
import java.util.List;

public class EnhancedVisitedResumeRepositoryImpl implements EnhancedVisitedResumeRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Job> findVisitedJobs(String resumeUuid, int offset, int limit) {
        TypedAggregation<VisitedResume> aggregation = new TypedAggregation<>(
            VisitedResume.class,
            Aggregation.match(Criteria.where("resumeUuid").is(resumeUuid)),
            Aggregation.project("visitedJobs").and("visitedJobs").slice(limit, offset)
        );
        VisitedResume visitedResume = mongoTemplate.aggregate(aggregation, VisitedResume.class, VisitedResume.class)
            .getUniqueMappedResult();
        return new ArrayList<>(visitedResume.getVisitedJobs());
    }
}
