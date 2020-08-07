package com.kuaidaoresume.resume.repository;

import com.kuaidaoresume.resume.model.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    Collection<Keyword> findByLanguage(String language);
}
