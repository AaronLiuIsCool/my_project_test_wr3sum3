package com.kuaidaoresume.matching.repo;

import com.kuaidaoresume.matching.model.Resume;

import java.util.List;

public interface EnhancedVisitedJobRepository {
    List<Resume> findVisitedResumes(String jobUuid, int offset, int limit);

}
