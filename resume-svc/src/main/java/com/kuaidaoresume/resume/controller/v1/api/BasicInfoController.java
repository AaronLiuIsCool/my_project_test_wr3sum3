package com.kuaidaoresume.resume.controller.v1.api;

import com.kuaidaoresume.common.auth.AuthConstant;
import com.kuaidaoresume.common.auth.Authorize;
import com.kuaidaoresume.resume.controller.v1.assembler.BasicInfoRepresentationModelAssembler;
import com.kuaidaoresume.resume.dto.BasicInfoDto;
import com.kuaidaoresume.resume.dto.PersistedBasicInfoDto;
import com.kuaidaoresume.resume.model.BasicInfo;
import com.kuaidaoresume.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Links;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class BasicInfoController {

    @Autowired
    private ResumeService resumeService;
    @Autowired
    private final BasicInfoRepresentationModelAssembler basicInfoAssembler;
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
    @GetMapping("/basicInfos/{id}")
    public ResponseEntity<EntityModel<PersistedBasicInfoDto>> findById(@PathVariable Long id) {
        return resumeService.findBasicInfoById(id)
            .map(basicInfo -> modelMapper.map(basicInfo, PersistedBasicInfoDto.class))
            .map(basicInfoAssembler::toModel)
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
    @GetMapping("/resumes/{resumeId}/basicInfo")
    public ResponseEntity<EntityModel<PersistedBasicInfoDto>> findByResumeId(@PathVariable String resumeId) {
        return resumeService.findBasicInfoByResumeId(resumeId)
            .map(basicInfo -> modelMapper.map(basicInfo, PersistedBasicInfoDto.class))
            .map(basicInfoAssembler::toModel)
            .map(entityModel -> {
                Links links = entityModel.getLinks().merge(Links.MergeMode.REPLACE_BY_REL,
                    linkTo(methodOn(BasicInfoController.class).findByResumeId(resumeId)).withSelfRel());
                return ResponseEntity.ok(EntityModel.of(entityModel.getContent(), links));
            })
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
    @PostMapping("/resumes/{resumeId}/basicInfo")
    public ResponseEntity<EntityModel<PersistedBasicInfoDto>> create(
        @PathVariable
        String resumeId,
        @Valid
        @RequestBody
        BasicInfoDto basicInfoDto) {

        BasicInfo basicInfo = modelMapper.map(basicInfoDto, BasicInfo.class);
        BasicInfo savedBasicInfo = resumeService.saveBasicInfo(resumeId, basicInfo);
        EntityModel<PersistedBasicInfoDto> entityModel =
            basicInfoAssembler.toModel(modelMapper.map(savedBasicInfo, PersistedBasicInfoDto.class));
        return ResponseEntity.created(basicInfoAssembler.getSelfLink(entityModel).toUri()).body(entityModel);
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
    @PutMapping("/resumes/{resumeId}/basicInfo")
    public ResponseEntity<EntityModel<PersistedBasicInfoDto>> save(
            @PathVariable
            String resumeId,
            @Valid
            @RequestBody
            PersistedBasicInfoDto basicInfoDto) {

        BasicInfo basicInfo = modelMapper.map(basicInfoDto, BasicInfo.class);
        BasicInfo savedBasicInfo = resumeService.saveBasicInfo(resumeId, basicInfo);
        EntityModel<PersistedBasicInfoDto> entityModel =
            basicInfoAssembler.toModel(modelMapper.map(savedBasicInfo, PersistedBasicInfoDto.class));
        return ResponseEntity.noContent().location(basicInfoAssembler.getSelfLink(entityModel).toUri()).build();
    }
}
