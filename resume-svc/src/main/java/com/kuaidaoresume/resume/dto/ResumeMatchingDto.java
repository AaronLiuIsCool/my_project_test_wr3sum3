package com.kuaidaoresume.resume.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kuaidaoresume.common.dto.LocationDto;
import com.kuaidaoresume.common.dto.PersistedEntityDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
@AllArgsConstructor
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Relation(itemRelation = "resumes", collectionRelation = "resumes")
public class ResumeMatchingDto implements PersistedEntityDto<String>  {

    @NotNull
    private String resumeUuid;

    private LocationDto location;

    private Collection<String> majors;

    private Collection<String> keywords;

    public String getId() {
        return resumeUuid;
    }
}
