package com.kuaidaoresume.resume.controller.v1.api;

import com.kuaidaoresume.common.auth.AuthConstant;
import com.kuaidaoresume.common.auth.Authorize;
import com.kuaidaoresume.resume.controller.v1.assembler.ResumeMatchingRepresentationModelAssembler;
import com.kuaidaoresume.resume.controller.v1.assembler.ResumeRatingRepresentationModelAssembler;
import com.kuaidaoresume.resume.controller.v1.assembler.ResumeRepresentationModelAssembler;
import com.kuaidaoresume.resume.controller.v1.assembler.ResumeScoreRepresentationModelAssembler;
import com.kuaidaoresume.resume.dto.*;
import com.kuaidaoresume.resume.model.Resume;
import com.kuaidaoresume.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class ResumeController {

    @Autowired
    private final ResumeService resumeService;

    @Autowired
    private final ResumeRepresentationModelAssembler resumeAssembler;

    @Autowired
    private final ResumeScoreRepresentationModelAssembler resumeScoreAssembler;

    @Autowired
    private final ResumeMatchingRepresentationModelAssembler resumeMatchingAssembler;

    @Autowired
    private final ResumeRatingRepresentationModelAssembler resumeRatingAssembler;

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
    @GetMapping("/resumes/{id}")
    public ResponseEntity<EntityModel<PersistedResumeDto>> findById(@PathVariable String id) {
        return resumeService.findResumeById(id)
            .map(resume -> modelMapper.map(resume, PersistedResumeDto.class))
            .map(resumeAssembler::toModel)
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
    @GetMapping("/resumes/{id}/score")
    public ResponseEntity<EntityModel<ResumeScoreDto>> getScore(@PathVariable String id) {
        return resumeService.getResumeScore(id)
            .map(resumeScoreAssembler::toModel)
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
    @PostMapping("/resumes")
    public ResponseEntity<EntityModel<PersistedResumeDto>> create(@Valid @RequestBody ResumeDto resumeDto) {

        Resume toSave = modelMapper.map(resumeDto, Resume.class);
        Resume saved = resumeService.saveResume(toSave);
        EntityModel<PersistedResumeDto> entityModel = resumeAssembler.toModel(modelMapper.map(saved, PersistedResumeDto.class));
        return ResponseEntity.created(resumeAssembler.getSelfLink(entityModel).toUri()).body(entityModel);
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
    @GetMapping("/resumes/{id}/matching")
    public ResponseEntity<EntityModel<ResumeMatchingDto>> getResumeMatching(@PathVariable String id) {
        return resumeService.getResumeMatching(id)
            .map(resumeMatchingAssembler::toModel)
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
    @GetMapping("/resumes/{id}/rating")
    public ResponseEntity<EntityModel<ResumeRatingDto>> getRating(@PathVariable String id) {
        return resumeService.getResumeRating(id)
            .map(resumeRatingAssembler::toModel)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
