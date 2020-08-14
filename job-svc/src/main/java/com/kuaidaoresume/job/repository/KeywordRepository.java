package com.kuaidaoresume.job.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.kuaidaoresume.job.model.Keyword;

import java.util.Optional;


@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    Optional<Keyword> findByName(String name);
    Optional<Keyword> deleteByName(String name);
}
