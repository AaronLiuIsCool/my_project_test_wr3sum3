package com.kuaidaoresume.resume.repository;

import com.kuaidaoresume.resume.model.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {
    List<Education> findAllByResumeId(String resumeId);
}
