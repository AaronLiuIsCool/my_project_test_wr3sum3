package com.kuaidaoresume.matching.repo;

import com.kuaidaoresume.matching.model.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;

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
                Criteria.where("keywords.value").in(keywords),
                Criteria.where("relevantMajors").in(majors)
            ),
            Criteria.where("isActive").is(true)
        ));
        query.with(Sort.by(Sort.Direction.DESC, "postDate"));
        return mongoTemplate.find(query, Job.class);
    }

    @Override
    public List<Job> searchJobs(String country, String city, String term, String language) {
        Query query = buildTextSearchQuery(country, city, term, language);
        return mongoTemplate.find(query, Job.class);
    }

    @Override
    public List<Job> searchJobs(String country, String city, String term, String language, int page, int pageSize) {
        Query query = buildTextSearchQuery(country, city, term, language);
        query.with(PageRequest.of(page, pageSize));
        return mongoTemplate.find(query, Job.class);
    }

    private Query buildTextSearchQuery(String country, String city, String term, String language) {
        TextCriteria textCriteria = TextCriteria.forLanguage(language).matching(term);
        TextQuery query = TextQuery.queryText(textCriteria).sortByScore();
        query.addCriteria(new Criteria().andOperator(
            Criteria.where("location.country").is(country),
            Criteria.where("location.city").is(city),
            Criteria.where("isActive").is(true)));
        query.with(Sort.by(Sort.Direction.DESC, "keywords.rating"));
        query.with(Sort.by(Sort.Direction.DESC, "postDate"));
        return query;
    }
}
