package com.kuaidaoresume.resume.controller.v1.assembler;

import com.kuaidaoresume.resume.controller.v1.api.WorkExperienceController;
import com.kuaidaoresume.resume.dto.PersistedWorkExperienceDto;
import org.springframework.stereotype.Component;

@Component
public class WorkExperienceRepresentationModelAssembler extends GenericRepresentationModelAssembler<PersistedWorkExperienceDto> {
    public WorkExperienceRepresentationModelAssembler() {
        super(WorkExperienceController.class);
    }
}
