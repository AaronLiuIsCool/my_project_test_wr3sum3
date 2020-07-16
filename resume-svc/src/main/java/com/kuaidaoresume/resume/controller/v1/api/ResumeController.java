package com.kuaidaoresume.resume.controller.v1.api;

import com.kuaidaoresume.common.auth.AuthConstant;
import com.kuaidaoresume.common.auth.Authorize;
import com.kuaidaoresume.resume.controller.v1.assembler.BasicInfoRepresentationModelAssembler;
import com.kuaidaoresume.resume.dto.BasicInfoDto;
import com.kuaidaoresume.resume.model.BasicInfo;
import com.kuaidaoresume.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/v1/resumes")
@RequiredArgsConstructor
public class ResumeController {

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
    @GetMapping("/{resumeId}/basicInfo")
    public ResponseEntity<EntityModel<BasicInfoDto>> findBasicInfo(@PathVariable String resumeId) {
        return resumeService.findBasicInfoByResumeId(resumeId)
            .map(basicInfo -> modelMapper.map(basicInfo, BasicInfoDto.class))
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
    @PostMapping("/{resumeId}/basicInfo")
    public ResponseEntity<EntityModel<BasicInfoDto>> createBasicInfo(
        @PathVariable
        String resumeId,
        @Valid
        @RequestBody
        BasicInfoDto basicInfoDto) {

        BasicInfo savedBasicInfo = resumeService.saveBasicInfo(resumeId, basicInfoDto);
        EntityModel<BasicInfoDto> entityModel = basicInfoAssembler.toModel(modelMapper.map(savedBasicInfo, BasicInfoDto.class));
        Optional<Link> resumeLink = entityModel.getLink("resumes");
        if (resumeLink.isPresent()) {
            Link link = resumeLink.get();
            return ResponseEntity.created(link.toUri()).body(entityModel);
        } else {
            return ResponseEntity.notFound().build();
        }
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
    @PutMapping("/{resumeId}/basicInfo")
    public ResponseEntity<EntityModel<BasicInfoDto>> updateBasicInfo(
            @PathVariable
            String resumeId,
            @Valid
            @RequestBody
            BasicInfoDto basicInfoDto) {

        BasicInfo savedBasicInfo = resumeService.saveBasicInfo(resumeId, basicInfoDto);
        EntityModel<BasicInfoDto> entityModel = basicInfoAssembler.toModel(modelMapper.map(savedBasicInfo, BasicInfoDto.class));
        Optional<Link> resumeLink = entityModel.getLink("resumes");
        if (resumeLink.isPresent()) {
            Link link = resumeLink.get();
            return ResponseEntity.noContent().location(link.toUri()).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
