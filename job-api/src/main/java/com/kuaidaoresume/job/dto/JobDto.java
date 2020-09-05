package com.kuaidaoresume.job.dto;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Date;
import java.util.Set;

/**
 * job DTO
 * @author Ruichen Teng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobDto {
    @NotNull
    private Date postDate;
    @NotNull
    private String positionTitle;
    @NotNull
    private String companyName;
    @NotNull
    private String url;
    private String agency;
    private int salaryMin;
    private int salaryMax;
    private String currency;
    private int yearExperienceRequired;
    private String certificateRequired;
    private String educationRequired;
    private String jobPostId;
    private String jobDescription;
    public LocationDto location;
    public List<MajorDto> majors;
    private Set<JobHasKeywordDto> jobHasKeywords = new HashSet<>();
}