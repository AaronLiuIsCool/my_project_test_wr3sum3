package com.kuaidaoresume.resume.controller.v1.api;

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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/v1/resumes")
@RequiredArgsConstructor
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    private final BasicInfoRepresentationModelAssembler basicInfoAssembler;

    @Autowired
    private final ModelMapper modelMapper;

    @GetMapping("/{resumeId}/basicInfo")
    public ResponseEntity<EntityModel<BasicInfoDto>> findBasicInfo(@PathVariable String resumeId) {
        return resumeService.findBasicInfoByResumeId(resumeId)
            .map(basicInfo -> modelMapper.map(basicInfo, BasicInfoDto.class))
            .map(basicInfoAssembler::toModel)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{resumeId}/basicInfo")
    public ResponseEntity<EntityModel<BasicInfoDto>> createBasicInfo(
        @PathVariable
        String resumeId,
        @Valid
        @RequestBody
        BasicInfoDto basicInfoDto) {

        BasicInfo savedBasicInfo = resumeService.saveBasicInfo(resumeId, basicInfoDto);
        EntityModel<BasicInfoDto> entityModel = basicInfoAssembler.toModel(modelMapper.map(savedBasicInfo, BasicInfoDto.class));
        Optional<Link> resumeLink = entityModel.getLink("resume");
        if (resumeLink.isPresent()) {
            Link link = resumeLink.get();
            return ResponseEntity.created(link.toUri()).body(entityModel);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{resumeId}/basicInfo")
    public ResponseEntity<EntityModel<BasicInfoDto>> updateBasicInfo(
            @PathVariable
            String resumeId,
            @Valid
            @RequestBody
            BasicInfoDto basicInfoDto) {

        BasicInfo savedBasicInfo = resumeService.saveBasicInfo(resumeId, basicInfoDto);
        EntityModel<BasicInfoDto> entityModel = basicInfoAssembler.toModel(modelMapper.map(savedBasicInfo, BasicInfoDto.class));
        Optional<Link> resumeLink = entityModel.getLink("resume");
        if (resumeLink.isPresent()) {
            Link link = resumeLink.get();
            return ResponseEntity.noContent().location(link.toUri()).build();
        }
        return ResponseEntity.notFound().build();
    }
}
