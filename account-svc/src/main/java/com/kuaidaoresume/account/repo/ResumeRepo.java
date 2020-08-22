package com.kuaidaoresume.account.repo;

import com.kuaidaoresume.account.model.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeRepo extends JpaRepository<Resume, String> {
}
