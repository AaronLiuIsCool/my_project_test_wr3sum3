package com.kuaidaoresume.resume.controller.v1.assembler;

import com.kuaidaoresume.resume.controller.v1.api.ProjectExperienceController;
import com.kuaidaoresume.resume.dto.PersistedProjectExperienceDto;
import org.springframework.stereotype.Component;

@Component
public class ProjectExperienceRepresentationModelAssembler extends GenericRepresentationModelAssembler<PersistedProjectExperienceDto> {
    public ProjectExperienceRepresentationModelAssembler() {
        super(ProjectExperienceController.class);
    }
}
