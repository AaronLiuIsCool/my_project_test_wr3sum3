package com.kuaidaoresume.resume.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kuaidaoresume.common.dto.PersistedEntityDto;
import lombok.Data;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Relation(itemRelation = "resumes", collectionRelation = "resumes")
public class PersistedResumeDto implements PersistedEntityDto<String> {

    @NotNull
    private String id;

    @NotNull
    private String language;

    private PersistedBasicInfoDto basicInfo;

    private Collection<PersistedEducationDto> educations;

    private Collection<PersistedWorkExperienceDto> workExperiences;

    private Collection<PersistedProjectExperienceDto> projectExperiences;

    private Collection<PersistedVolunteerExperienceDto> volunteerExperiences;

    private Collection<PersistedCertificateDto> certificates;

    private String photoReference;
}
