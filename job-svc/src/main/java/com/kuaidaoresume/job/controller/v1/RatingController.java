package com.kuaidaoresume.job.controller.v1;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import com.kuaidaoresume.common.auth.AuthConstant;
import com.kuaidaoresume.common.auth.Authorize;
import com.kuaidaoresume.job.controller.assembler.RatingRepresentationModelAssembler;
import com.kuaidaoresume.job.service.RatingService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.hateoas.EntityModel;
import org.springframework.beans.factory.annotation.Autowired;


import com.kuaidaoresume.job.dto.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor

public class RatingController {

    static final ILogger logger = SLoggerFactory.getLogger(RatingController.class);

    @Autowired
    private RatingService ratingService;

    @Autowired
    private final ModelMapper modelMapper;

    @Autowired
    private final RatingRepresentationModelAssembler ratingRepresentationModelAssembler;


    @Authorize(value = {
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE,
        AuthConstant.AUTHORIZATION_WHOAMI_SERVICE,
        AuthConstant.AUTHORIZATION_BOT_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @PutMapping("/ratings/job")
    public ResponseEntity<EntityModel<PersistedRatingDto>> updateJobRating(
            @Valid @RequestBody
                    JobHasKeywordDto jobHasKeywordDto) {
        logger.info("jobHasKeywordDto = " + jobHasKeywordDto);
        Optional<JobHasKeywordDto> newRating = ratingService.updateJobRating(jobHasKeywordDto);
        if (newRating.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @Authorize(value = {
            AuthConstant.AUTHORIZATION_WWW_SERVICE,
            AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE,
            AuthConstant.AUTHORIZATION_WHOAMI_SERVICE,
            AuthConstant.AUTHORIZATION_BOT_SERVICE,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @DeleteMapping("/ratings/job")
    public ResponseEntity<EntityModel<PersistedRatingDto>> deleteJobRating(
            @Valid @RequestBody
                    JobHasKeywordDto jobHasKeywordDto) {
        logger.info("jobHasKeywordDto = " + jobHasKeywordDto);
        Optional<JobHasKeywordDto> newRating = ratingService.deleteJobRating(jobHasKeywordDto);
        if (newRating.isPresent()) {
            return ResponseEntity.accepted().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @Authorize(value = {
            AuthConstant.AUTHORIZATION_WWW_SERVICE,
            AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE,
            AuthConstant.AUTHORIZATION_WHOAMI_SERVICE,
            AuthConstant.AUTHORIZATION_BOT_SERVICE,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @PostMapping("/ratings/job")
    public ResponseEntity<EntityModel<PersistedRatingDto>> createJobRating(
            @Valid @RequestBody
                    JobHasKeywordDto jobHasKeywordDto) {
        logger.info("jobHasKeywordDto = " + jobHasKeywordDto);
        Optional<JobHasKeywordDto> newRating = ratingService.addJobRating(jobHasKeywordDto);
        if (newRating.isPresent()) {
            EntityModel<PersistedRatingDto> entityModel =
                    ratingRepresentationModelAssembler.toModel(modelMapper.map(newRating, PersistedRatingDto.class));
            return ResponseEntity.created(ratingRepresentationModelAssembler.getSelfLink(entityModel).toUri()).body(entityModel);
        }
        return ResponseEntity.badRequest().build();
    }

    @Authorize(value = {
            AuthConstant.AUTHORIZATION_WWW_SERVICE,
            AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE,
            AuthConstant.AUTHORIZATION_WHOAMI_SERVICE,
            AuthConstant.AUTHORIZATION_BOT_SERVICE,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @PutMapping("/ratings/location")
    public ResponseEntity<EntityModel<PersistedRatingDto>> updateLocationRating(
            @Valid @RequestBody
                    LocationHasKeywordDto locationHasKeywordDto) {
        logger.info("locationHasKeywordDto = " + locationHasKeywordDto);
        Optional<LocationHasKeywordDto> newRating = ratingService.updateLocationRating(locationHasKeywordDto);
        if (newRating.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @Authorize(value = {
            AuthConstant.AUTHORIZATION_WWW_SERVICE,
            AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE,
            AuthConstant.AUTHORIZATION_WHOAMI_SERVICE,
            AuthConstant.AUTHORIZATION_BOT_SERVICE,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @DeleteMapping("/ratings/location")
    public ResponseEntity<EntityModel<PersistedRatingDto>> deleteLocationRating(
            @Valid @RequestBody
                    LocationHasKeywordDto locationHasKeywordDto) {
        logger.info("locationHasKeywordDto = " + locationHasKeywordDto);
        Optional<LocationHasKeywordDto> newRating = ratingService.deleteLocationRating(locationHasKeywordDto);
        if (newRating.isPresent()) {
            return ResponseEntity.accepted().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @Authorize(value = {
            AuthConstant.AUTHORIZATION_WWW_SERVICE,
            AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE,
            AuthConstant.AUTHORIZATION_WHOAMI_SERVICE,
            AuthConstant.AUTHORIZATION_BOT_SERVICE,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @PostMapping("/ratings/location")
    public ResponseEntity<EntityModel<PersistedRatingDto>> createLocationRating(
            @Valid @RequestBody
                    LocationHasKeywordDto locationHasKeywordDto) {
        logger.info("locationHasKeywordDto = " + locationHasKeywordDto);
        Optional<LocationHasKeywordDto> newRating = ratingService.addLocationRating(locationHasKeywordDto);
        if (newRating.isPresent()) {
            EntityModel<PersistedRatingDto> entityModel =
                    ratingRepresentationModelAssembler.toModel(modelMapper.map(newRating, PersistedRatingDto.class));
            return ResponseEntity.created(ratingRepresentationModelAssembler.getSelfLink(entityModel).toUri()).body(entityModel);
        }
        return ResponseEntity.badRequest().build();
    }

    @Authorize(value = {
            AuthConstant.AUTHORIZATION_WWW_SERVICE,
            AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE,
            AuthConstant.AUTHORIZATION_WHOAMI_SERVICE,
            AuthConstant.AUTHORIZATION_BOT_SERVICE,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @PutMapping("/ratings/major")
    public ResponseEntity<EntityModel<PersistedRatingDto>> updateMajorRating(
            @RequestBody @Valid
                    MajorHasKeywordDto majorHasKeywordDto) {
        logger.info("majorHasKeywordDto = " + majorHasKeywordDto);
        Optional<MajorHasKeywordDto> newRating = ratingService.updateMajorRating(majorHasKeywordDto);
        if (newRating.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @Authorize(value = {
            AuthConstant.AUTHORIZATION_WWW_SERVICE,
            AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE,
            AuthConstant.AUTHORIZATION_WHOAMI_SERVICE,
            AuthConstant.AUTHORIZATION_BOT_SERVICE,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @DeleteMapping("/ratings/major")
    public ResponseEntity<EntityModel<PersistedRatingDto>> deleteMajorRating(
            @Valid @RequestBody
                    MajorHasKeywordDto majorHasKeywordDto) {
        logger.info("majorHasKeywordDto = " + majorHasKeywordDto);
        Optional<MajorHasKeywordDto> newRating = ratingService.deleteMajorRating(majorHasKeywordDto);
        if (newRating.isPresent()) {
            return ResponseEntity.accepted().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/ratings/major")
    public ResponseEntity<EntityModel<PersistedRatingDto>> createMajorRating(
            @Valid @RequestBody
                    MajorHasKeywordDto majorHasKeywordDto) {
        logger.info("majorHasKeywordDto = " + majorHasKeywordDto);
        Optional<MajorHasKeywordDto> newRating = ratingService.addMajorRating(majorHasKeywordDto);
        if (newRating.isPresent()) {
            EntityModel<PersistedRatingDto> entityModel =
                    ratingRepresentationModelAssembler.toModel(modelMapper.map(newRating, PersistedRatingDto.class));
            return ResponseEntity.created(ratingRepresentationModelAssembler.getSelfLink(entityModel).toUri()).body(entityModel);
        }
        return ResponseEntity.badRequest().build();
    }
}
