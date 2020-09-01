package com.kuaidaoresume.resume.controller.v1.api;

import com.kuaidaoresume.common.auth.AuthConstant;
import com.kuaidaoresume.common.auth.Authorize;
import com.kuaidaoresume.resume.controller.v1.assembler.WorkExperienceRepresentationModelAssembler;
import com.kuaidaoresume.resume.dto.ExperienceDto;
import com.kuaidaoresume.resume.dto.PersistedWorkExperienceDto;
import com.kuaidaoresume.resume.model.WorkExperience;
import com.kuaidaoresume.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Links;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class WorkExperienceController {

    @Autowired
    private final ResumeService resumeService;
    @Autowired
    private final WorkExperienceRepresentationModelAssembler assembler;
    @Autowired
    private final ModelMapper modelMapper;

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE,
        //AuthConstant.AUTHORIZATION_WHOAMI_SERVICE,
        //AuthConstant.AUTHORIZATION_BOT_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @GetMapping("/work-experiences/{id}")
    public ResponseEntity<EntityModel<PersistedWorkExperienceDto>> findById(@PathVariable Long id) {
        return resumeService.findById(id, WorkExperience.class)
            .map(workExperience -> modelMapper.map(workExperience, PersistedWorkExperienceDto.class))
            .map(assembler::toModel)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE,
        //AuthConstant.AUTHORIZATION_WHOAMI_SERVICE,
        //AuthConstant.AUTHORIZATION_BOT_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @GetMapping("/resumes/{resumeId}/work-experiences")
    public ResponseEntity<CollectionModel<EntityModel<PersistedWorkExperienceDto>>> findAllByResumeId(
        @PathVariable String resumeId) {

        List<PersistedWorkExperienceDto> dtos = resumeService.findAllByResumeId(resumeId, WorkExperience.class).stream()
            .map(workExperience -> modelMapper.map(workExperience, PersistedWorkExperienceDto.class)).collect(Collectors.toList());
        CollectionModel<EntityModel<PersistedWorkExperienceDto>> collectionModel = assembler.toCollectionModel(dtos);

        Links links = collectionModel.getLinks().merge(Links.MergeMode.REPLACE_BY_REL,
            linkTo(methodOn(WorkExperienceController.class).findAllByResumeId(resumeId)).withSelfRel());
        return ResponseEntity.ok(CollectionModel.of(collectionModel.getContent(), links));
    }

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE,
        //AuthConstant.AUTHORIZATION_WHOAMI_SERVICE,
        //AuthConstant.AUTHORIZATION_BOT_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @PostMapping("/resumes/{resumeId}/work-experiences")
    public ResponseEntity<EntityModel<PersistedWorkExperienceDto>> create(
        @PathVariable String resumeId,
        @Valid @RequestBody ExperienceDto experienceDto) {

        WorkExperience workExperience = modelMapper.map(experienceDto, WorkExperience.class);
        WorkExperience saved = resumeService.newWorkExperience(resumeId, workExperience);
        EntityModel<PersistedWorkExperienceDto> entityModel =
            assembler.toModel(modelMapper.map(saved, PersistedWorkExperienceDto.class));
        return ResponseEntity.created(assembler.getSelfLink(entityModel).toUri()).body(entityModel);
    }

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE,
        //AuthConstant.AUTHORIZATION_WHOAMI_SERVICE,
        //AuthConstant.AUTHORIZATION_BOT_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @PutMapping("/work-experiences/{id}")
    public ResponseEntity<EntityModel<PersistedWorkExperienceDto>> save(
        @PathVariable Long id,
        @Valid @RequestBody PersistedWorkExperienceDto experienceDto) {

        WorkExperience toUpdate = modelMapper.map(experienceDto, WorkExperience.class);
        toUpdate.setId(id);
        resumeService.updateResumeContainable(toUpdate, WorkExperience.class);
        EntityModel<PersistedWorkExperienceDto> entityModel =
            assembler.toModel(modelMapper.map(toUpdate, PersistedWorkExperienceDto.class));
        return ResponseEntity.noContent().location(assembler.getSelfLink(entityModel).toUri()).build();
    }

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE,
        //AuthConstant.AUTHORIZATION_WHOAMI_SERVICE,
        //AuthConstant.AUTHORIZATION_BOT_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @PutMapping("/resumes/{resumeId}/work-experiences")
    public ResponseEntity<CollectionModel<EntityModel<PersistedWorkExperienceDto>>> saveAll(
        @PathVariable String resumeId,
        @Valid @RequestBody List<PersistedWorkExperienceDto> experienceDtosBatch) {

        Iterable<WorkExperience> batchToUpdate = experienceDtosBatch.stream()
            .map(experienceDto -> modelMapper.map(experienceDto, WorkExperience.class))
            .collect(Collectors.toList());
        Collection<WorkExperience> saved = resumeService.saveWorkExperiences(resumeId, batchToUpdate);
        CollectionModel<EntityModel<PersistedWorkExperienceDto>> collectionModel = assembler
            .toCollectionModel(saved.stream().map(workExperience ->
            modelMapper.map(workExperience, PersistedWorkExperienceDto.class))
            .collect(Collectors.toList()));
        return ResponseEntity.noContent().location(
            collectionModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).build();
    }

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE,
        //AuthConstant.AUTHORIZATION_WHOAMI_SERVICE,
        //AuthConstant.AUTHORIZATION_BOT_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @DeleteMapping("/work-experiences/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        resumeService.deleteById(id, WorkExperience.class);
        return ResponseEntity.noContent().build();
    }

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE,
        //AuthConstant.AUTHORIZATION_WHOAMI_SERVICE,
        //AuthConstant.AUTHORIZATION_BOT_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @DeleteMapping("/resumes/{resumeId}/work-experiences")
    public ResponseEntity<?> deleteAllByResumeId(@PathVariable String resumeId) {
        resumeService.deleteAllByResumeId(resumeId, WorkExperience.class);
        return ResponseEntity.noContent().build();
    }
}
