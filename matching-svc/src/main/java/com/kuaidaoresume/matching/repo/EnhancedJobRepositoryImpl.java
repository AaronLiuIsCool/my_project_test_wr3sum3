package com.kuaidaoresume.matching.repo;

import com.kuaidaoresume.matching.model.Job;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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
    public List<Job> searchJobs(String country, String city, String term) {
        Query query = buildTextSearchQuery(country, city, term);
        return mongoTemplate.find(query, Job.class);
    }

    @Override
    @Cacheable("searchJobs-paging")
    public List<Job> searchJobs(String country, String city, String term, int page, int pageSize) {
        Query query = buildTextSearchQuery(country, city, term);
        query.with(PageRequest.of(page, pageSize));
        return mongoTemplate.find(query, Job.class);
    }

    @Override
    public long countJobsMatchedByTerm(String country, String city, String term) {
        Query query = buildTextSearchQuery(country, city, term);
        return mongoTemplate.count(query, Job.class);
    }

    // TODO: SL write stress tests for this query
    private Query buildTextSearchQuery(String country, String city, String term) {
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage().matching(term);
        TextQuery query = TextQuery.queryText(textCriteria).sortByScore();
        Criteria searchCriteria = new Criteria().and("isActive").is(true);
        if (StringUtils.isNotEmpty(country)) {
            searchCriteria.and("location.country").is(country);
        }
        if (StringUtils.isNotEmpty(city)) {
            searchCriteria.and("location.city").is(city);
        }
        query.addCriteria(searchCriteria);
        query.with(Sort.by(Sort.Direction.DESC, "keywords.rating"));
        query.with(Sort.by(Sort.Direction.DESC, "postDate"));
        return query;
    }
}
