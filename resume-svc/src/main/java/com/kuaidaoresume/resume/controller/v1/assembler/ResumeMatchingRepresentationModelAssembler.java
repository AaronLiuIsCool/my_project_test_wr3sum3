package com.kuaidaoresume.resume.controller.v1.assembler;

import com.kuaidaoresume.common.assembler.v1.GenericRepresentationModelAssembler;
import com.kuaidaoresume.resume.controller.v1.api.ResumeController;
import com.kuaidaoresume.resume.dto.ResumeMatchingDto;
import org.springframework.stereotype.Component;

@Component
public class ResumeMatchingRepresentationModelAssembler extends GenericRepresentationModelAssembler<ResumeMatchingDto> {
    public ResumeMatchingRepresentationModelAssembler() {
        super(ResumeController.class);
    }
}
