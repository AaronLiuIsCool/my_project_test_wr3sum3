package com.kuaidaoresume.resume.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kuaidaoresume.common.dto.PersistedEntityDto;
import com.kuaidaoresume.resume.service.rating.Rating;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Relation(itemRelation = "educations", collectionRelation = "educations")
public class EducationRatingDto implements PersistedEntityDto<Long>  {

    @NotNull
    private Long id;
    @NotNull
    private String institution;
    @NotNull
    private String major;
    private String gpa;
    @NotNull
    private Rating rating;
}
