package com.kuaidaoresume.resume.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
@AllArgsConstructor
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResumeDto {

    @NotNull
    private String language;

    private BasicInfoDto basicInfo;

    private Collection<EducationDto> educations;

    private Collection<ExperienceDto> workExperiences;

    private Collection<ExperienceDto> projectExperiences;

    private Collection<ExperienceDto> volunteerExperiences;

    private Collection<CertificateDto> certificates;

    private String photoReference;
}
