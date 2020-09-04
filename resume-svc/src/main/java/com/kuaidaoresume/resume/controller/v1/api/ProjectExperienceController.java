package com.kuaidaoresume.resume.controller.v1.api;

import com.kuaidaoresume.common.auth.AuthConstant;
import com.kuaidaoresume.common.auth.Authorize;
import com.kuaidaoresume.resume.controller.v1.assembler.ProjectExperienceRepresentationModelAssembler;
import com.kuaidaoresume.resume.dto.ExperienceDto;
import com.kuaidaoresume.resume.dto.PersistedProjectExperienceDto;
import com.kuaidaoresume.resume.model.ProjectExperience;
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

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class ProjectExperienceController {

    @Autowired
    private final ResumeService resumeService;
    @Autowired
    private final ProjectExperienceRepresentationModelAssembler assembler;
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
    @GetMapping("/project-experiences/{id}")
    public ResponseEntity<EntityModel<PersistedProjectExperienceDto>> findById(@PathVariable Long id) {
        return resumeService.findById(id, ProjectExperience.class)
            .map(experience -> modelMapper.map(experience, PersistedProjectExperienceDto.class))
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
    @GetMapping("/resumes/{resumeId}/project-experiences")
    public ResponseEntity<CollectionModel<EntityModel<PersistedProjectExperienceDto>>> findAllByResumeId(
        @PathVariable String resumeId) {

        List<PersistedProjectExperienceDto> dtos = resumeService.findAllByResumeId(resumeId, ProjectExperience.class).stream()
            .map(ProjectExperience -> modelMapper.map(ProjectExperience, PersistedProjectExperienceDto.class)).collect(Collectors.toList());
        CollectionModel<EntityModel<PersistedProjectExperienceDto>> collectionModel = assembler.toCollectionModel(dtos);

        Links links = collectionModel.getLinks().merge(Links.MergeMode.REPLACE_BY_REL,
            linkTo(methodOn(ProjectExperienceController.class).findAllByResumeId(resumeId)).withSelfRel());
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
    @PostMapping("/resumes/{resumeId}/project-experiences")
    public ResponseEntity<EntityModel<PersistedProjectExperienceDto>> create(
        @PathVariable String resumeId,
        @Valid @RequestBody ExperienceDto experienceDto) {

        ProjectExperience ProjectExperience = modelMapper.map(experienceDto, ProjectExperience.class);
        ProjectExperience saved = resumeService.newProjectExperience(resumeId, ProjectExperience);
        EntityModel<PersistedProjectExperienceDto> entityModel =
            assembler.toModel(modelMapper.map(saved, PersistedProjectExperienceDto.class));
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
    @PutMapping("/project-experiences/{id}")
    public ResponseEntity<EntityModel<PersistedProjectExperienceDto>> save(
        @PathVariable Long id,
        @Valid @RequestBody PersistedProjectExperienceDto experienceDto) {

        ProjectExperience toUpdate = modelMapper.map(experienceDto, ProjectExperience.class);
        toUpdate.setId(id);
        resumeService.updateResumeContainable(toUpdate, ProjectExperience.class);
        EntityModel<PersistedProjectExperienceDto> entityModel =
            assembler.toModel(modelMapper.map(toUpdate, PersistedProjectExperienceDto.class));
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
    @PutMapping("/resumes/{resumeId}/project-experiences")
    public ResponseEntity<CollectionModel<EntityModel<PersistedProjectExperienceDto>>> saveAll(
        @PathVariable String resumeId,
        @Valid @RequestBody List<PersistedProjectExperienceDto> experienceDtosBatch) {

        Iterable<ProjectExperience> batchToUpdate = experienceDtosBatch.stream()
            .map(experienceDto -> modelMapper.map(experienceDto, ProjectExperience.class))
            .collect(Collectors.toList());
        Collection<ProjectExperience> saved = resumeService.saveProjectExperiences(resumeId, batchToUpdate);
        CollectionModel<EntityModel<PersistedProjectExperienceDto>> collectionModel = assembler
            .toCollectionModel(saved.stream().map(ProjectExperience ->
                modelMapper.map(ProjectExperience, PersistedProjectExperienceDto.class))
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
    @DeleteMapping("/project-experiences/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        resumeService.deleteById(id, ProjectExperience.class);
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
    @DeleteMapping("/resumes/{resumeId}/project-experiences")
    public ResponseEntity<?> deleteAllByResumeId(@PathVariable String resumeId) {
        resumeService.deleteAllByResumeId(resumeId, ProjectExperience.class);
        return ResponseEntity.noContent().build();
    }
}
