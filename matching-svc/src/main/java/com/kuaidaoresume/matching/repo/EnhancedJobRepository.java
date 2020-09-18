package com.kuaidaoresume.matching.repo;

import com.kuaidaoresume.matching.model.Job;

import java.util.Collection;
import java.util.List;

public interface EnhancedJobRepository {
    List<Job> findMatchedJobs(String country, String city, Collection<String> majors, Collection<String> keywords);

    List<Job> searchJobs(String country, String city, String term, String language);

    List<Job> searchJobs(String country, String city, String term, String language, int page, int pageSize);
}
