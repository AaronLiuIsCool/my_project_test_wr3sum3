package com.kuaidaoresume.matching.repo;

import com.kuaidaoresume.matching.model.Resume;

import java.util.List;

public interface EnhancedTailoredJobRepository {
    List<Resume> findTailoredResumesWithLimit(String jobUuid, int offset, int limit);
}
