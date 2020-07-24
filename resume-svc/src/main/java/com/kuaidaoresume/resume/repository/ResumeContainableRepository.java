package com.kuaidaoresume.resume.repository;

import com.kuaidaoresume.resume.model.ResumeContainable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@NoRepositoryBean
@Transactional
public interface ResumeContainableRepository<T extends ResumeContainable> extends JpaRepository<T, Long> {
    Collection<T> findAllByResumeId(String resumeId);

    Optional<T> findByResumeId(String resumeId);
}
