package com.kuaidaoresume.job.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * job DTO
 * @author Ruichen Teng
 */
@Data
@NoArgsConstructor
@SuperBuilder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobDto extends SimpleJobDto{
    private LocationDto location;
    private List<MajorDto> majors;
    private Set<JobHasKeywordDto> jobHasKeywords = new HashSet<>();
}