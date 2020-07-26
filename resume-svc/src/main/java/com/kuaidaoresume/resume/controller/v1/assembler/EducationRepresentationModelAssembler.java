package com.kuaidaoresume.resume.controller.v1.assembler;

import com.kuaidaoresume.common.assembler.v1.GenericRepresentationModelAssembler;
import com.kuaidaoresume.resume.controller.v1.api.EducationController;
import com.kuaidaoresume.resume.dto.PersistedEducationDto;
import org.springframework.stereotype.Component;

@Component
public class EducationRepresentationModelAssembler extends GenericRepresentationModelAssembler<PersistedEducationDto> {
    public EducationRepresentationModelAssembler() {
        super(EducationController.class);
    }
}
