package com.kuaidaoresume.resume.controller.v1.api;

import com.kuaidaoresume.common.auth.AuthConstant;
import com.kuaidaoresume.common.auth.Authorize;
import com.kuaidaoresume.resume.controller.v1.assembler.VolunteerExperienceRepresentationModelAssembler;
import com.kuaidaoresume.resume.dto.ExperienceDto;
import com.kuaidaoresume.resume.dto.PersistedVolunteerExperienceDto;
import com.kuaidaoresume.resume.model.VolunteerExperience;
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
public class VolunteerExperienceController {

    @Autowired
    private final ResumeService resumeService;
    @Autowired
    private final VolunteerExperienceRepresentationModelAssembler assembler;
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
    @GetMapping("/volunteer-experiences/{id}")
    public ResponseEntity<EntityModel<PersistedVolunteerExperienceDto>> findById(@PathVariable Long id) {
        return resumeService.findById(id, VolunteerExperience.class)
            .map(experience -> modelMapper.map(experience, PersistedVolunteerExperienceDto.class))
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
    @GetMapping("/resumes/{resumeId}/volunteer-experiences")
    public ResponseEntity<CollectionModel<EntityModel<PersistedVolunteerExperienceDto>>> findAllByResumeId(
        @PathVariable String resumeId) {

        List<PersistedVolunteerExperienceDto> dtos = resumeService.findAllByResumeId(resumeId, VolunteerExperience.class).stream()
            .map(VolunteerExperience -> modelMapper.map(VolunteerExperience, PersistedVolunteerExperienceDto.class)).collect(Collectors.toList());
        CollectionModel<EntityModel<PersistedVolunteerExperienceDto>> collectionModel = assembler.toCollectionModel(dtos);

        Links links = collectionModel.getLinks().merge(Links.MergeMode.REPLACE_BY_REL,
            linkTo(methodOn(VolunteerExperienceController.class).findAllByResumeId(resumeId)).withSelfRel());
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
    @PostMapping("/resumes/{resumeId}/volunteer-experiences")
    public ResponseEntity<EntityModel<PersistedVolunteerExperienceDto>> create(
        @PathVariable String resumeId,
        @Valid @RequestBody ExperienceDto experienceDto) {

        VolunteerExperience VolunteerExperience = modelMapper.map(experienceDto, VolunteerExperience.class);
        VolunteerExperience saved = resumeService.newVolunteerExperience(resumeId, VolunteerExperience);
        EntityModel<PersistedVolunteerExperienceDto> entityModel =
            assembler.toModel(modelMapper.map(saved, PersistedVolunteerExperienceDto.class));
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
    @PutMapping("/volunteer-experiences/{id}")
    public ResponseEntity<EntityModel<PersistedVolunteerExperienceDto>> save(
        @PathVariable Long id,
        @Valid @RequestBody PersistedVolunteerExperienceDto experienceDto) {

        VolunteerExperience toUpdate = modelMapper.map(experienceDto, VolunteerExperience.class);
        toUpdate.setId(id);
        resumeService.save(toUpdate, VolunteerExperience.class);
        EntityModel<PersistedVolunteerExperienceDto> entityModel =
            assembler.toModel(modelMapper.map(toUpdate, PersistedVolunteerExperienceDto.class));
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
    @PutMapping("/resumes/{resumeId}/volunteer-experiences")
    public ResponseEntity<CollectionModel<EntityModel<PersistedVolunteerExperienceDto>>> saveAll(
        @PathVariable String resumeId,
        @Valid @RequestBody List<PersistedVolunteerExperienceDto> experienceDtosBatch) {

        Iterable<VolunteerExperience> batchToUpdate = experienceDtosBatch.stream()
            .map(experienceDto -> modelMapper.map(experienceDto, VolunteerExperience.class))
            .collect(Collectors.toList());
        Collection<VolunteerExperience> saved = resumeService.saveVolunteerExperiences(resumeId, batchToUpdate);
        CollectionModel<EntityModel<PersistedVolunteerExperienceDto>> collectionModel = assembler
            .toCollectionModel(saved.stream().map(VolunteerExperience ->
                modelMapper.map(VolunteerExperience, PersistedVolunteerExperienceDto.class))
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
    @DeleteMapping("/volunteer-experiences/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        resumeService.deleteById(id, VolunteerExperience.class);
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
    @DeleteMapping("/resumes/{resumeId}/volunteer-experiences")
    public ResponseEntity<?> deleteAllByResumeId(@PathVariable String resumeId) {
        resumeService.deleteAllByResumeId(resumeId, VolunteerExperience.class);
        return ResponseEntity.noContent().build();
    }
}
