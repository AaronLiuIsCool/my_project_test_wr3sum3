package com.kuaidaoresume.job.repository;

import com.kuaidaoresume.job.model.Suggestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {

    Suggestion findSuggestionById(Long id);

    @Query(
        value = "SELECT * FROM suggestion s WHERE s.industry = :industry and s.position_title = :position_title",
        countQuery = "SELECT count(*) FROM suggestion s WHERE s.industry = :industry and s.position_title = :position_title",
        nativeQuery = true)
    Page<Suggestion> findSuggestionByIndustryAndAndPositionTitle(@Param("industry") String industry, @Param("position_title") String positionTitle, Pageable pageRequest);

}
