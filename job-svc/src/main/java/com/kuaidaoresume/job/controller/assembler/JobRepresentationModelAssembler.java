package com.kuaidaoresume.job.controller.assembler;

import com.kuaidaoresume.job.controller.v1.JobController;
import com.kuaidaoresume.job.dto.PersistedJobDto;
import com.kuaidaoresume.common.assembler.v1.GenericRepresentationModelAssembler;

import org.springframework.stereotype.Component;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.kuaidaoresume.job.dto.JobDto;

@Component
public class JobRepresentationModelAssembler extends GenericRepresentationModelAssembler<PersistedJobDto> {
    public JobRepresentationModelAssembler() {
        super(JobController.class);
    }
}