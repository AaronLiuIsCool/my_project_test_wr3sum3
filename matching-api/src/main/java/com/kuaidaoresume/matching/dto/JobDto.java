package com.kuaidaoresume.matching.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobDto {

    @NotNull
    private String jobUuid;

    @NotNull
    private String title;

    @NotNull
    private String companyName;

    private LocationDto location;

    private String jobType;

    private String employmentType;

    private CompensationDto compensation;

    private Collection<String> relevantMajors;

    private Collection<KeywordDto> keywords;

    private String url;

    @NotNull
    private LocalDateTime postDate;
}
