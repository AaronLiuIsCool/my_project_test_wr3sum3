package com.kuaidaoresume.resume.repository;

import com.kuaidaoresume.resume.model.Award;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestAwardRepository extends CrudRepository<Award, Long> {
    Award findByEducationId(Long educationId);
}
