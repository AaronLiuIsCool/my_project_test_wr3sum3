package com.kuaidaoresume.resume.controller.v1.assembler;

import com.kuaidaoresume.common.assembler.v1.GenericRepresentationModelAssembler;
import com.kuaidaoresume.resume.controller.v1.api.ResumeController;
import com.kuaidaoresume.resume.dto.ResumeRatingDto;
import org.springframework.stereotype.Component;

@Component
public class ResumeRatingRepresentationModelAssembler extends GenericRepresentationModelAssembler<ResumeRatingDto> {
    public ResumeRatingRepresentationModelAssembler() {
        super(ResumeController.class);
    }
}
