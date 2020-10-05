package com.kuaidaoresume.matching.repo;

import com.kuaidaoresume.matching.model.Resume;
import com.kuaidaoresume.matching.model.VisitedJob;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EnhancedVisitedJobRepositoryImpl implements EnhancedVisitedJobRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Resume> findVisitedResumes(String jobUuid, int offset, int limit) {
        TypedAggregation<VisitedJob> aggregation = new TypedAggregation<>(
            VisitedJob.class,
            Aggregation.match(Criteria.where("jobUuid").is(jobUuid)),
            Aggregation.project("visitedResumes").and("visitedResumes").slice(limit, offset)
        );
        VisitedJob visitedJob = mongoTemplate.aggregate(aggregation, VisitedJob.class, VisitedJob.class)
            .getUniqueMappedResult();
        return Objects.nonNull(visitedJob) ? new ArrayList<>(visitedJob.getVisitedResumes()) : Lists.newArrayList();
    }
}
