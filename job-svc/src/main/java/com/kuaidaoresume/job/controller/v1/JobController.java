package com.kuaidaoresume.job.controller.v1;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import com.kuaidaoresume.common.auth.AuthConstant;
import com.kuaidaoresume.common.auth.Authorize;
import com.kuaidaoresume.job.model.JobHasKeyword;
import com.kuaidaoresume.job.service.JobInfoExtractionService;
import com.kuaidaoresume.job.service.JobService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.beans.factory.annotation.Autowired;

import com.kuaidaoresume.job.dto.*;
import com.kuaidaoresume.job.controller.assembler.JobRepresentationModelAssembler;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class JobController {

    static final ILogger logger = SLoggerFactory.getLogger(JobController.class);

    @Autowired
    private JobService jobService;

    @Autowired
    private JobInfoExtractionService jobInfoExtractionService;

    @Autowired
    private final JobRepresentationModelAssembler jobRepresentationModelAssembler;

    @Autowired
    private final ModelMapper modelMapper;

    @Authorize(value = {
            AuthConstant.AUTHORIZATION_WWW_SERVICE,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @GetMapping("/jobs/{id}")
    public ResponseEntity<EntityModel<PersistedJobDto>> findJob(@PathVariable long id) {
        return jobService.findJobById(id)
                .map(job -> modelMapper.map(job, PersistedJobDto.class))
                .map(jobRepresentationModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Authorize(value = {
            AuthConstant.AUTHORIZATION_WWW_SERVICE,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @GetMapping("/jobs/uuid/{uuid}")
    public ResponseEntity<EntityModel<PersistedJobDto>> findJob(@PathVariable String uuid,
                                                                      @RequestParam(required = false, defaultValue = "true") boolean lazy) {
        if(lazy)
        return jobService.findSimpleJobByUuid(uuid)
                .map(job -> modelMapper.map(job, PersistedJobDto.class))
                .map(jobRepresentationModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

        return jobService.findJobByUuid(uuid)
                .map(job -> modelMapper.map(job, PersistedJobDto.class))
                .map(jobRepresentationModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Authorize(value = {
            AuthConstant.AUTHORIZATION_WWW_SERVICE,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @DeleteMapping("jobs/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Long id) {
        jobService.deleteJobById(id);
        return ResponseEntity.accepted().build();
    }

    @Authorize(value = {
            AuthConstant.AUTHORIZATION_WWW_SERVICE,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @DeleteMapping("/jobs")
    public ResponseEntity<?> deleteAllJobs() {
        jobService.deleteAllJobs();
        return ResponseEntity.accepted().build();
    }

    @Authorize(value = {
            AuthConstant.AUTHORIZATION_WWW_SERVICE,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @PostMapping("/jobs")
    public ResponseEntity<EntityModel<PersistedJobDto>> createJob(
            @Valid @RequestBody
                    JobDto jobDto) {
        logger.info("jobDto = " + jobDto);
        Optional<JobDto> newJob = jobService.createJob(jobDto);
        if (newJob.isPresent()) {
            EntityModel<PersistedJobDto> entityModel =
                    jobRepresentationModelAssembler.toModel(modelMapper.map(newJob, PersistedJobDto.class));
            return ResponseEntity.created(jobRepresentationModelAssembler.getSelfLink(entityModel).toUri()).body(entityModel);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/jobs/jobFetcher")
    public JobFetcherResponse jobFetcher(
            @Valid @RequestBody
                    JobFetcherRequest jobFetcherRequest) {
        logger.info("jobFetcherRequest = " + jobFetcherRequest);

        JobFetcherResponse jobFetcherResponse = jobInfoExtractionService.extractAndPersist(jobFetcherRequest);

        return jobFetcherResponse;
    }

    @Authorize(value = {
            AuthConstant.AUTHORIZATION_WWW_SERVICE,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @PutMapping("/jobs")
    public ResponseEntity<EntityModel<PersistedJobDto>> updateJob(
            @Valid @RequestBody
                    JobDto jobDto) {
        logger.info("jobDto = " + jobDto);
        Optional<JobDto> updatedJob = jobService.updateJob(jobDto);
        if (updatedJob.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @Authorize(value = {
            AuthConstant.AUTHORIZATION_WWW_SERVICE,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
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

    @Authorize(value = {
            AuthConstant.AUTHORIZATION_WWW_SERVICE,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @PostMapping("/jobs/job-search")
    public ResponseEntity<CollectionModel<EntityModel<PersistedJobDto>>> findByLocationAndMajor(@RequestBody JobSearchDto searchDto) {
        logger.info("major = " + searchDto.getMajors() +  " locations = " + searchDto.getLocations());
        List<JobDto> foundJobs = null;
        if(searchDto.getLocations() != null && searchDto.getMajors() != null) {
           foundJobs = jobService.findJobByLocationAndMajor(searchDto.getLocations(), searchDto.getMajors());
        }
        else if(searchDto.getLocations() != null) {
            foundJobs = jobService.findJobByLocation(searchDto.getLocations());
        }
        else if(searchDto.getMajors() != null) {
            foundJobs = jobService.findJobByMajor(searchDto.getMajors());
        }
        if(foundJobs != null && foundJobs.size() > 0) {
            return ResponseEntity.ok(jobRepresentationModelAssembler.toCollectionModel((foundJobs.stream().map(jobDto -> modelMapper.map(jobDto, PersistedJobDto.class))
                    .collect(Collectors.toList()))));
        }
        return ResponseEntity.notFound().build();
    }
}
