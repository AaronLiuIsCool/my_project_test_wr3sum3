package com.kuaidaoresume.resume.controller.v1.api;

import com.kuaidaoresume.common.auth.AuthConstant;
import com.kuaidaoresume.common.auth.Authorize;
import com.kuaidaoresume.resume.controller.v1.assembler.EducationRepresentationModelAssembler;
import com.kuaidaoresume.resume.dto.EducationDto;
import com.kuaidaoresume.resume.dto.PersistedEducationDto;
import com.kuaidaoresume.resume.model.Education;
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
public class EducationController {

    @Autowired
    private final ResumeService resumeService;
    @Autowired
    private final EducationRepresentationModelAssembler educationAssembler;
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
    @GetMapping("/educations/{id}")
    public ResponseEntity<EntityModel<PersistedEducationDto>> findById(@PathVariable Long id) {
        return resumeService.findById(id, Education.class)
            .map(education -> modelMapper.map(education, PersistedEducationDto.class))
            .map(educationAssembler::toModel)
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
    @GetMapping("/resumes/{resumeId}/educations")
    public ResponseEntity<CollectionModel<EntityModel<PersistedEducationDto>>> findAllByResumeId(
        @PathVariable String resumeId) {

        List<PersistedEducationDto> dtos = resumeService.findAllByResumeId(resumeId, Education.class).stream()
        .map(education -> modelMapper.map(education, PersistedEducationDto.class)).collect(Collectors.toList());
        CollectionModel<EntityModel<PersistedEducationDto>> collectionModel = educationAssembler.toCollectionModel(dtos);

        Links links = collectionModel.getLinks().merge(Links.MergeMode.REPLACE_BY_REL,
            linkTo(methodOn(EducationController.class).findAllByResumeId(resumeId)).withSelfRel());
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
    @PostMapping("/resumes/{resumeId}/educations")
    public ResponseEntity<EntityModel<PersistedEducationDto>> create(
        @PathVariable
        String resumeId,
        @Valid
        @RequestBody
        EducationDto educationDto) {

        Education education = modelMapper.map(educationDto, Education.class);
        Education saved = resumeService.newEducation(resumeId, education);
        EntityModel<PersistedEducationDto> entityModel =
            educationAssembler.toModel(modelMapper.map(saved, PersistedEducationDto.class));
        return ResponseEntity.created(educationAssembler.getSelfLink(entityModel).toUri()).body(entityModel);
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
    @PutMapping("/educations/{id}")
    public ResponseEntity<EntityModel<PersistedEducationDto>> save(
        @PathVariable
        Long id,
        @Valid
        @RequestBody
        PersistedEducationDto educationDto) {

        Education toUpdate = modelMapper.map(educationDto, Education.class);
        toUpdate.setId(id);
        resumeService.save(toUpdate, Education.class);
        EntityModel<PersistedEducationDto> entityModel =
            educationAssembler.toModel(modelMapper.map(toUpdate, PersistedEducationDto.class));
        return ResponseEntity.noContent().location(educationAssembler.getSelfLink(entityModel).toUri()).build();
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
    @PutMapping("/resumes/{resumeId}/educations")
    public ResponseEntity<CollectionModel<EntityModel<PersistedEducationDto>>> saveAll(
        @PathVariable
        String resumeId,
        @Valid
        @RequestBody
        List<PersistedEducationDto> educationDtoBatch) {

        Iterable<Education> batchToUpdate = educationDtoBatch.stream()
            .map(educationDto -> modelMapper.map(educationDto, Education.class))
            .collect(Collectors.toList());
        Collection<Education> saved = resumeService.saveEducations(resumeId, batchToUpdate);
        CollectionModel<EntityModel<PersistedEducationDto>> collectionModel = educationAssembler
            .toCollectionModel(saved.stream().map(education -> modelMapper.map(education, PersistedEducationDto.class))
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
    @DeleteMapping("/educations/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        resumeService.deleteById(id, Education.class);
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
    @DeleteMapping("/resumes/{resumeId}/educations")
    public ResponseEntity<?> deleteAllByResumeId(@PathVariable String resumeId) {
        resumeService.deleteAllByResumeId(resumeId, Education.class);
        return ResponseEntity.noContent().build();
    }
}
