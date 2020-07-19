package com.kuaidaoresume.resume.repository;

import com.kuaidaoresume.resume.model.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface TestProfileRepository extends CrudRepository<Profile, Long> {
}
