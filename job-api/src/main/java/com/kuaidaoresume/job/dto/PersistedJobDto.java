package com.kuaidaoresume.job.dto;

import com.kuaidaoresume.common.dto.PersistedEntityDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@SuperBuilder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Relation(itemRelation = "job", collectionRelation = "jobs")
public class PersistedJobDto extends JobDto implements PersistedEntityDto<Long> {
    @NotNull
    private Long id;
}
