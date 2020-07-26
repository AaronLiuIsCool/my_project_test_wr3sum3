package com.kuaidaoresume.job.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.kuaidaoresume.job.model.Job;
import com.kuaidaoresume.job.model.Keyword;
import com.kuaidaoresume.job.model.Location;
import com.kuaidaoresume.job.model.Major;

import java.util.List;
import java.util.Optional;

@Repository
public interface MajorRepository extends JpaRepository<Major, Long> {
    Optional<Major> findByNameIgnoreCase(String name);
}
