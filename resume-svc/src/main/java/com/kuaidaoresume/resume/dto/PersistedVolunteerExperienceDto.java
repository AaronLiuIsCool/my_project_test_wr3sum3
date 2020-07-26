package com.kuaidaoresume.resume.dto;

import com.kuaidaoresume.common.dto.PersistedEntityDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotNull;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Relation(itemRelation = "volunteer-experiences", collectionRelation = "volunteer-experiences")
public class PersistedVolunteerExperienceDto extends ExperienceDto implements PersistedEntityDto<Long> {
    @NotNull
    private Long id;
}
