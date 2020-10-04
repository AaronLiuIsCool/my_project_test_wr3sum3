package com.kuaidaoresume.matching.repo;

import com.kuaidaoresume.matching.model.Resume;
import com.kuaidaoresume.matching.model.TailoredJob;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EnhancedTailoredJobRepositoryImpl implements EnhancedTailoredJobRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Resume> findTailoredResumesWithLimit(String jobUuid, int offset, int limit) {
        TypedAggregation<TailoredJob> aggregation = new TypedAggregation<>(
            TailoredJob.class,
            Aggregation.match(Criteria.where("jobUuid").is(jobUuid)),
            Aggregation.project("tailoredResumes").and("tailoredResumes").slice(limit, offset)
        );
        TailoredJob tailoredJob = mongoTemplate.aggregate(aggregation, TailoredJob.class, TailoredJob.class)
            .getUniqueMappedResult();
        return Objects.nonNull(tailoredJob) ? new ArrayList<>(tailoredJob.getTailoredResumes()) : Lists.newArrayList();
    }
}
