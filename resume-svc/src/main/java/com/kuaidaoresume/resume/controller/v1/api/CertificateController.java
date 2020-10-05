package com.kuaidaoresume.resume.controller.v1.api;

import com.kuaidaoresume.common.auth.AuthConstant;
import com.kuaidaoresume.common.auth.Authorize;
import com.kuaidaoresume.resume.controller.v1.assembler.CertificateRepresentationModelAssembler;
import com.kuaidaoresume.resume.dto.CertificateDto;
import com.kuaidaoresume.resume.dto.PersistedCertificateDto;
import com.kuaidaoresume.resume.model.Certificate;
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
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class CertificateController {

    @Autowired
    private final ResumeService resumeService;
    @Autowired
    private final CertificateRepresentationModelAssembler assembler;
    @Autowired
    private final ModelMapper modelMapper;

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @GetMapping("/certificates/{id}")
    public ResponseEntity<EntityModel<PersistedCertificateDto>> findById(@PathVariable Long id) {
        return resumeService.findById(id, Certificate.class)
            .map(certificate -> modelMapper.map(certificate, PersistedCertificateDto.class))
            .map(assembler::toModel)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @GetMapping("/resumes/{resumeId}/certificates")
    public ResponseEntity<CollectionModel<EntityModel<PersistedCertificateDto>>> findAllByResumeId(
        @PathVariable String resumeId) {

        List<PersistedCertificateDto> dtos = resumeService.findAllByResumeId(resumeId, Certificate.class).stream()
            .map(certificate -> modelMapper.map(certificate, PersistedCertificateDto.class)).collect(Collectors.toList());
        CollectionModel<EntityModel<PersistedCertificateDto>> collectionModel = assembler.toCollectionModel(dtos);

        Links links = collectionModel.getLinks().merge(Links.MergeMode.REPLACE_BY_REL,
            linkTo(methodOn(CertificateController.class).findAllByResumeId(resumeId)).withSelfRel());
        return ResponseEntity.ok(CollectionModel.of(collectionModel.getContent(), links));
    }

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @PostMapping("/resumes/{resumeId}/certificates")
    public ResponseEntity<EntityModel<PersistedCertificateDto>> create(
        @PathVariable String resumeId,
        @Valid @RequestBody CertificateDto certificateDto) {

        Certificate certificate = modelMapper.map(certificateDto, Certificate.class);
        Certificate saved = resumeService.newCertificate(resumeId, certificate);
        EntityModel<PersistedCertificateDto> entityModel =
            assembler.toModel(modelMapper.map(saved, PersistedCertificateDto.class));
        return ResponseEntity.created(assembler.getSelfLink(entityModel).toUri()).body(entityModel);
    }

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @PutMapping("/certificates/{id}")
    public ResponseEntity<EntityModel<PersistedCertificateDto>> save(
        @PathVariable Long id,
        @Valid @RequestBody PersistedCertificateDto certificateDto) {

        Certificate toUpdate = modelMapper.map(certificateDto, Certificate.class);
        toUpdate.setId(id);
        resumeService.updateResumeContainable(toUpdate, Certificate.class);
        EntityModel<PersistedCertificateDto> entityModel =
            assembler.toModel(modelMapper.map(toUpdate, PersistedCertificateDto.class));
        return ResponseEntity.noContent().location(assembler.getSelfLink(entityModel).toUri()).build();
    }

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @PutMapping("/resumes/{resumeId}/certificates")
    public ResponseEntity<CollectionModel<EntityModel<PersistedCertificateDto>>> saveAll(
        @PathVariable String resumeId,
        @Valid @RequestBody List<PersistedCertificateDto> certificateDtosBatch) {

        Iterable<Certificate> batchToUpdate = certificateDtosBatch.stream()
            .map(certificateDto -> modelMapper.map(certificateDto, Certificate.class))
            .collect(Collectors.toList());
        Collection<Certificate> saved = resumeService.saveCertificates(resumeId, batchToUpdate);
        CollectionModel<EntityModel<PersistedCertificateDto>> collectionModel = assembler
            .toCollectionModel(saved.stream().map(certificate ->
                modelMapper.map(certificate, PersistedCertificateDto.class))
                .collect(Collectors.toList()));
        return ResponseEntity.noContent().location(
            collectionModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).build();
    }

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @DeleteMapping("/certificates/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id, @RequestParam @NotNull String resumeId) {
        resumeService.deleteById(id, Certificate.class, resumeId);
        return ResponseEntity.noContent().build();
    }

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @DeleteMapping("/resumes/{resumeId}/certificates")
    public ResponseEntity<?> deleteAllByResumeId(@PathVariable String resumeId) {
        resumeService.deleteAllByResumeId(resumeId, Certificate.class);
        return ResponseEntity.noContent().build();
    }
}
