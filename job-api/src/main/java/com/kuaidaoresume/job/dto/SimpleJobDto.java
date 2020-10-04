package com.kuaidaoresume.job.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;

import java.util.Date;

/**
 * Simple job DTO
 * @author Ruichen Teng
 */
@AllArgsConstructor
@RequiredArgsConstructor
@Data
@SuperBuilder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SimpleJobDto {
    private String uuid;
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
    @JsonProperty("jobPostId")
    private String jobPostIdentifier;
    private String jobDescription;
}