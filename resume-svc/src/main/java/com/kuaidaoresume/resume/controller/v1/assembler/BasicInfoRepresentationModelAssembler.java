package com.kuaidaoresume.resume.controller.v1.assembler;

import com.kuaidaoresume.resume.controller.v1.api.ResumeController;
import com.kuaidaoresume.resume.dto.BasicInfoDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BasicInfoRepresentationModelAssembler implements SimpleRepresentationModelAssembler<BasicInfoDto> {

    @Override
    public void addLinks(EntityModel<BasicInfoDto> resource) {
        String resumeId = resource.getContent().getResumeId();
        resource.add(linkTo(methodOn(ResumeController.class).findBasicInfo(resumeId)).withRel("resumes"));
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<BasicInfoDto>> resources) {
    }
}
