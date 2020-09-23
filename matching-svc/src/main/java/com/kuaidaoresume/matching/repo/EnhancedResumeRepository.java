package com.kuaidaoresume.matching.repo;

import com.kuaidaoresume.matching.model.Resume;

import java.util.Collection;
import java.util.List;

public interface EnhancedResumeRepository {
    List<Resume> findMatchedResumes(String country, String city, Collection<String> majors, Collection<String> keywords);

    List<Resume> findMatchedResumes(String country, String city, Collection<String> majors, Collection<String> keywords,
        int page, int pageSize);

    long countMatchedResumes(String country, String city, Collection<String> majors, Collection<String> keywords);
}
