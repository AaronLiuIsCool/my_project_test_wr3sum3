package com.kuaidaoresume.matching.repo;

import com.kuaidaoresume.matching.model.Job;

import java.util.List;

public interface EnhancedBookmarkedResumeRepository {
    List<Job> findBookmarkedJobs(String resumeUuid, int offset, int limit);
}
