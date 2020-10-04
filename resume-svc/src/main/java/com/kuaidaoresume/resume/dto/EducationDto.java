package com.kuaidaoresume.resume.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EducationDto {

    @NotNull
    private String country;
    @NotNull
    private String city;
    @NotNull
    private String institution;
    @NotNull
    private String major;
    @NotNull
    private String degree;
    @NotNull
    private String startDate;
    private String endDate;
    private String gpa;
    private List<AwardDto> awards;
}
