package com.kuaidaoresume.matching.repo;

import com.kuaidaoresume.matching.model.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collection;
import java.util.List;

public class EnhancedJobRepositoryImpl implements EnhancedJobRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Job> findMatchedJobs(String country, String city, Collection<String> majors, Collection<String> keywords) {
        Query query = new Query();
        query.addCriteria(new Criteria().andOperator(
            Criteria.where("location.country").is(country),
            Criteria.where("location.city").is(city),
            new Criteria().orOperator(
                Criteria.where("relevantMajors").in(majors),
                Criteria.where("keywords").in(keywords)
            ),
            Criteria.where("isActive").is(true)
        ));
        query.with(Sort.by(Sort.Direction.DESC, "postDate"));
        return mongoTemplate.find(query, Job.class);
    }
}
