package com.kuaidaoresume.job.controller.assembler;

import com.kuaidaoresume.job.controller.v1.JobController;
import com.kuaidaoresume.job.dto.PersistedJobDto;
import com.kuaidaoresume.common.assembler.v1.GenericRepresentationModelAssembler;

import org.springframework.stereotype.Component;

@Component
public class JobRepresentationModelAssembler extends GenericRepresentationModelAssembler<PersistedJobDto> {
    public JobRepresentationModelAssembler() {
        super(JobController.class);
    }
}