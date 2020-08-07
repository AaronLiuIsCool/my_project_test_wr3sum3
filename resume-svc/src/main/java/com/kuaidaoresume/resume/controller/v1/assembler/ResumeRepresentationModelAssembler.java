package com.kuaidaoresume.resume.controller.v1.assembler;

import com.kuaidaoresume.common.assembler.v1.GenericRepresentationModelAssembler;
import com.kuaidaoresume.resume.controller.v1.api.ResumeController;
import com.kuaidaoresume.resume.dto.PersistedResumeDto;
import org.springframework.stereotype.Component;

@Component
public class ResumeRepresentationModelAssembler extends GenericRepresentationModelAssembler<PersistedResumeDto> {

    public ResumeRepresentationModelAssembler() {
        super(ResumeController.class);
    }
}
