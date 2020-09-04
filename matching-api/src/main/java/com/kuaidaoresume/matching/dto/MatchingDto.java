package com.kuaidaoresume.matching.dto;

import com.kuaidaoresume.common.dto.LocationDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchingDto {

    @NotBlank //verification TBD
    private String id;
    @NotBlank
    private String jobId;
    @NotBlank
    private String title;
    @NotBlank
    private String companyName;
    @NotBlank
    private LocationDto locationDto;

    private String jobType;
    private String employmentType;
    private CompensationDto compensationDto;

    @NotBlank
    private String major;
    @NotBlank
    private int age;

}
