package com.kuaidaoresume.job.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.Date;

/**
 * job DTO
 * @author Ruichen Teng
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
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
    private int salaryMin;
    private int salaryMax;
    private String currency;
    private int yearExperienceRequired;
    private String certificateRequired;
    private String educationRequired;
    private int jobPostId;
    public LocationDto location;
    public List<MajorDto> majors;
}