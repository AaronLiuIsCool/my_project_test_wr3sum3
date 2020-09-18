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
public class ResumeRatingDto implements PersistedEntityDto<String>  {

    @NotNull
    private String resumeUuid;

    @NotNull
    private int score;

    @NotNull
    private Collection<EducationRatingDto> educations;

    @NotNull
    private Collection<ExperienceRatingDto> workExperiences;

    @NotNull
    private Collection<ExperienceRatingDto> projectExperiences;

    @NotNull
    private Collection<ExperienceRatingDto> volunteerExperiences;

    @NotNull
    private int numCertificates;

    @Override
    public String getId() {
        return resumeUuid;
    }
}
