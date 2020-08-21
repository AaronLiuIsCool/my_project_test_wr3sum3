package com.kuaidaoresume.job.dto;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

import java.util.List;

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

public class JobFetcherRequest {
    @NotNull
    private String title;
    @NotNull
    @JsonProperty("company_name")
    private String companyName;
    @NotNull
    private String location;
    @JsonProperty("via")
    private String agency;
    private String description;
    private List<String> extensions;
    @JsonProperty("job_id")
    private String jobPostId;
    @JsonProperty("job_link")
    private String jobLink;
}
