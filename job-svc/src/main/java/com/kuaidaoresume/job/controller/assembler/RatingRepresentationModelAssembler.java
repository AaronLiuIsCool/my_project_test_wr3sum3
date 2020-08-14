package com.kuaidaoresume.job.controller.assembler;

import com.kuaidaoresume.job.controller.v1.RatingController;
import com.kuaidaoresume.job.dto.PersistedRatingDto;
import com.kuaidaoresume.common.assembler.v1.GenericRepresentationModelAssembler;

import org.springframework.stereotype.Component;

@Component
public class RatingRepresentationModelAssembler extends GenericRepresentationModelAssembler<PersistedRatingDto> {
    public RatingRepresentationModelAssembler() {
        super(RatingController.class);
    }
}