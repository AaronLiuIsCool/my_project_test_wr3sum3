package com.kuaidaoresume.matching.repo;

import com.kuaidaoresume.matching.model.Resume;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collection;
import java.util.List;

public class EnhancedResumeRepositoryImpl implements EnhancedResumeRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Resume> findMatchedResumes(String country, String city, Collection<String> majors, Collection<String> keywords) {
        Query query = buildFindMatchedResumesQuery(country, city, majors, keywords);
        return mongoTemplate.find(query, Resume.class);
    }

    @Override
    public List<Resume> findMatchedResumes(String country, String city, Collection<String> majors, Collection<String> keywords, int page, int pageSize) {
        Query query = buildFindMatchedResumesQuery(country, city, majors, keywords);
        query.with(PageRequest.of(page, pageSize));
        return mongoTemplate.find(query, Resume.class);
    }

    @Override
    public long countMatchedResumes(String country, String city, Collection<String> majors, Collection<String> keywords) {
        Query query = buildFindMatchedResumesQuery(country, city, majors, keywords);
        return mongoTemplate.count(query, Resume.class);
    }

    private Query buildFindMatchedResumesQuery(String country, String city, Collection<String> majors, Collection<String> keywords) {
        Query query = new Query();
        query.addCriteria(new Criteria().andOperator(
            Criteria.where("location.country").is(country),
            Criteria.where("location.city").is(city),
            new Criteria().orOperator(
                Criteria.where("keywords").in(keywords),
                Criteria.where("majors").in(majors)
            )
        ));
        return query;
    }
}
