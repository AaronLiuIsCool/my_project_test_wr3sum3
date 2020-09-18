package com.kuaidaoresume.resume.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kuaidaoresume.resume.service.rating.Rating;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExperienceRatingDto {

    @NotNull
    private String organization;
    @NotNull
    private String role;
    @NotNull
    private Rating rating;
    @NotNull
    private Collection<BulletDto> bullets;
}
