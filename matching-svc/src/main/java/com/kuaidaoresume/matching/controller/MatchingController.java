package com.kuaidaoresume.matching.controller;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import com.kuaidaoresume.common.api.BaseResponse;
import com.kuaidaoresume.common.auth.AuthConstant;
import com.kuaidaoresume.common.auth.Authorize;
import com.kuaidaoresume.common.env.EnvConfig;
import com.kuaidaoresume.matching.dto.GenericMatchingResponse;
import com.kuaidaoresume.matching.dto.ListMatchingResponse;
import com.kuaidaoresume.matching.dto.MatchingDto;
import com.kuaidaoresume.matching.dto.MatchingList;
import com.kuaidaoresume.matching.service.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/v1")
@Validated
public class MatchingController {

    static final ILogger logger = SLoggerFactory.getLogger(MatchingController.class);

    @Autowired
    private MatchingService matchingService;

    @Autowired
    private EnvConfig envConfig;

    @PostMapping(path = "/matchings")
    @Authorize(value = {
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
            AuthConstant.AUTHORIZATION_SUPPORT_USER
    })
    public GenericMatchingResponse createMatching(@RequestBody @Valid MatchingDto matchingDto) {
        MatchingDto aMatchingDTO = matchingService.createMatching(matchingDto);
        return new GenericMatchingResponse(aMatchingDTO);
    }

    @GetMapping(path = "/matchings/{matchingId}")
    @Authorize(value = {
            AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE,
            AuthConstant.AUTHORIZATION_WHOAMI_SERVICE,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_WWW_SERVICE
    })
    GenericMatchingResponse getMatching(@RequestParam @NotBlank String matchingId) {
        MatchingDto matchingDto = matchingService.getMatching(matchingId);
        GenericMatchingResponse genericMatchingResponse = new GenericMatchingResponse(matchingDto);
        return genericMatchingResponse;
    }

    @GetMapping(path = "/matchings/list")
    @Authorize(value = {
            AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE,
            AuthConstant.AUTHORIZATION_WHOAMI_SERVICE,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_WWW_SERVICE
    })
    ListMatchingResponse listMatching(@RequestParam int offset, @RequestParam @Min(0) int limit,
                                          @RequestParam String location, @RequestParam String major,
                                          @RequestParam String[] keywords) {
        MatchingList matchingList = matchingService.listMatchings(offset, limit, location, major, keywords);
        ListMatchingResponse listMatchingResponse = new ListMatchingResponse(matchingList);
        return listMatchingResponse;
    }

    @GetMapping(path = "/matchings/resumes")
    @Authorize(value = {
            AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE,
            AuthConstant.AUTHORIZATION_WHOAMI_SERVICE,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_WWW_SERVICE
    })
    GenericMatchingResponse getMatchingByResume(@RequestParam int offset, @RequestParam @Min(0) int  limit,
                                             @RequestParam @NotBlank String resumeId) {
        MatchingDto matchingDto = matchingService.getMatchingByResumeId(resumeId);
        return new GenericMatchingResponse(matchingDto);
    }

    @PutMapping(path = "/matchings")
    @Authorize(value = {
            AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE,
            AuthConstant.AUTHORIZATION_WHOAMI_SERVICE,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
            AuthConstant.AUTHORIZATION_SUPPORT_USER
    })
    GenericMatchingResponse updateMatching(@RequestBody @Valid MatchingDto matchingDto) {
        MatchingDto updateMatchingDto = matchingService.updateMatching(matchingDto);
        return new GenericMatchingResponse(updateMatchingDto);
    }

    @PatchMapping(path = "/matchings/{matchingId}/resumes")
    @Authorize(value = {
            AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE,
            AuthConstant.AUTHORIZATION_WHOAMI_SERVICE,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_WWW_SERVICE
    })
    GenericMatchingResponse updateMatchingByResume(@RequestParam @NotBlank String matchingId,
                                                   @RequestParam @NotBlank String resumeId,
                                                   @RequestParam @NotBlank String userId) {
        MatchingDto updateMatchingDto = matchingService.updateMatching(matchingId, resumeId, userId);
        return new GenericMatchingResponse(updateMatchingDto);
    }

    @PatchMapping(path = "/matchings/{matchingId}/inactive")
    @Authorize(value = {
            AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE,
            AuthConstant.AUTHORIZATION_WHOAMI_SERVICE,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
            AuthConstant.AUTHORIZATION_SUPPORT_USER
    })
    BaseResponse inactiveMatching(@RequestParam @NotBlank String matchingId) {
        matchingId = matchingService.inactiveMatching(matchingId);
        return BaseResponse.builder().message("Matching <" + matchingId + "> has been inactive.").build();
    }

}
