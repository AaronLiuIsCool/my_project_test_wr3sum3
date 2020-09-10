package com.kuaidaoresume.matching.repo;

import com.kuaidaoresume.matching.model.Resume;

import java.util.List;

public interface EnhancedBookmarkedJobRepository {
    List<Resume> findResumesBookmarkedByJob(String jobUuid, int offset, int limit);
}
