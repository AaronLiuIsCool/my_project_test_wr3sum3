package com.kuaidaoresume.matching.repo;

import com.kuaidaoresume.matching.model.Job;

import java.util.List;

public interface EnhancedVisitedResumeRepository {
    List<Job> findVisitedJobs(String resumeUuid, int offset, int limit);
}
