package com.kuaidaoresume.resume.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kuaidaoresume.common.dto.PersistedEntityDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
@AllArgsConstructor
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Relation(itemRelation = "resumes", collectionRelation = "resumes")
public class ResumeScoreDto implements PersistedEntityDto<String> {

    @NotNull
    private String resumeId;
    @NotNull
    private int completenessScore;
    @NotNull
    private int photoScore;
    @NotNull
    private int experiencesKeywordScore;
    @NotNull
    private int experiencesNumericWordScore;
    @NotNull
    private Collection<UniversityRankScoreDto> universityRankScores;
    @NotNull
    private Collection<GpaScoreDto> gpaScores;
    @NotNull
    private Collection<CompanyRankScoreDto> companyRankScores;
    @NotNull
    private int workExperiencesScore;
    @NotNull
    private Collection<WorkExperienceDurationScoreDto> workExperienceDurationScores;
    @NotNull
    private int projectExperiencesScore;
    @NotNull
    private Collection<ProjectExperienceDurationScoreDto> projectExperienceDurationScores;
    @NotNull
    private int certificatesScore;

    @Override
    public String getId() {
        return resumeId;
    }
}
