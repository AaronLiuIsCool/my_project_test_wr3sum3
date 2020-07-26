package com.kuaidaoresume.job.controller.v1;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import com.kuaidaoresume.job.service.JobService;
import com.kuaidaoresume.common.auth.AuthConstant;
import com.kuaidaoresume.common.auth.Authorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Links;

import com.kuaidaoresume.job.dto.*;
import com.kuaidaoresume.job.service.JobService;
import com.kuaidaoresume.job.controller.assembler.JobRepresentationModelAssembler;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import javax.validation.Valid;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor

public class JobController {

    static final ILogger logger = SLoggerFactory.getLogger(JobController.class);

    @Autowired
    private JobService jobService;

    @Autowired
    private final JobRepresentationModelAssembler jobRepresentationModelAssembler;

    @Autowired
    private final ModelMapper modelMapper;

//    @Authorize(value = {
//            AuthConstant.AUTHORIZATION_WWW_SERVICE,
//            AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE,
//            //AuthConstant.AUTHORIZATION_WHOAMI_SERVICE,
//            //AuthConstant.AUTHORIZATION_BOT_SERVICE,
//            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
//            AuthConstant.AUTHORIZATION_SUPPORT_USER,
//            AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
//    })

    @GetMapping("/jobs/{id}")
    public ResponseEntity<EntityModel<PersistedJobDto>> findJob(@PathVariable long id) {
        return jobService.findJobById(id)
                .map(job -> modelMapper.map(job, PersistedJobDto.class))
                .map(jobRepresentationModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("jobs/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Long id) {
        jobService.deleteJobById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/jobs/all")
    public ResponseEntity<CollectionModel<EntityModel<PersistedJobDto>>> findAllJobs() {
        //TODO@ruichen: add pagination, limit
        List<JobDto> foundJobs = jobService.findAll();
        if(foundJobs != null && foundJobs.size() > 0) {
            CollectionModel<EntityModel<PersistedJobDto>> collectionModel = jobRepresentationModelAssembler.
                            toCollectionModel((foundJobs.stream().map(jobDto -> modelMapper.map(jobDto, PersistedJobDto.class))
                            .collect(Collectors.toList())));
            return ResponseEntity.ok(CollectionModel.of(collectionModel.getContent()));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/jobs/job-search/location")
    public ResponseEntity<CollectionModel<EntityModel<PersistedJobDto>>> findJobByLocation(@RequestBody List<LocationDto> locations) {
        logger.info("location = " + locations);
        List<JobDto> foundJobs = jobService.findJobByLocation(locations);
        if(foundJobs != null && foundJobs.size() > 0) {
            return ResponseEntity.ok(jobRepresentationModelAssembler.toCollectionModel(foundJobs.stream().map(jobDto -> modelMapper.map(jobDto, PersistedJobDto.class))
                    .collect(Collectors.toList())));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/jobs/job-search/major")
    public ResponseEntity<CollectionModel<EntityModel<PersistedJobDto>>> findJobByMajor(@RequestBody List<MajorDto> majors) {
        logger.info("major = " + majors);
        List<JobDto> foundJobs = jobService.findJobByMajor(majors);
        if(foundJobs != null && foundJobs.size() > 0) {
            return ResponseEntity.ok(jobRepresentationModelAssembler.toCollectionModel((foundJobs.stream().map(jobDto -> modelMapper.map(jobDto, PersistedJobDto.class))
                    .collect(Collectors.toList()))));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/jobs/save")
    public ResponseEntity<EntityModel<PersistedJobDto>> saveJob(
            @Valid @RequestBody
                    JobDto jobDto) {
        logger.info("jobDto = " + jobDto);
        Optional<JobDto> savedJob = jobService.saveJob(jobDto);
        if(savedJob.isPresent()) {
            EntityModel<PersistedJobDto> entityModel =
                    jobRepresentationModelAssembler.toModel(modelMapper.map(savedJob, PersistedJobDto.class));
            return ResponseEntity.created(jobRepresentationModelAssembler.getSelfLink(entityModel).toUri()).body(entityModel);
        }
        return ResponseEntity.badRequest().build();
    }
}
