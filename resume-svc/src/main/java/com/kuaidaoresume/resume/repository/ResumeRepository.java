package com.kuaidaoresume.resume.repository;

import com.kuaidaoresume.resume.model.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ResumeRepository extends JpaRepository<Resume, String> {

}
