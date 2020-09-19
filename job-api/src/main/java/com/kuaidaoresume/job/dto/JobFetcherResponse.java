package com.kuaidaoresume.job.dto;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.Date;
import java.time.LocalDateTime;

/**
 * JobFetcherRequest DTO
 * @author Ruichen Teng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobFetcherResponse {
    @JsonProperty("jobUuid")
    String uuid;
    String title;
    String companyName;
    LocationDto location;
    String jobType;
    String employmentType;
    List<String> relevantMajors;
    Date createAt;
    @Builder.Default
    boolean isActive = true;
    List<KeywordDto> keywords;
    @NotNull
    private LocalDateTime postDate;
}