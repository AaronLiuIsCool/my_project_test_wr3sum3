package com.kuaidaoresume.matching.client;


import com.kuaidaoresume.common.api.BaseResponse;
import com.kuaidaoresume.common.auth.AuthConstant;
import com.kuaidaoresume.matching.MatchingConstant;
import com.kuaidaoresume.matching.dto.GenericMatchingResponse;
import com.kuaidaoresume.matching.dto.JobListResponse;
import com.kuaidaoresume.matching.dto.MatchingDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@FeignClient(name = MatchingConstant.SERVICE_NAME, path = "/v1", url = "${kuaidaoresume.matching-service-endpoint}")
public interface MatchingClient {

    @PostMapping(path = "/matchings")
    GenericMatchingResponse createMatching(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authz, @RequestBody @Valid MatchingDto matchingDto);

    @GetMapping(path = "/matchings/{id}")
    GenericMatchingResponse getMatching(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authz, @RequestParam @NotBlank String matchingId);

    @GetMapping(path = "/matchings/list")
    JobListResponse listMatching(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authz, @RequestParam int offset, @RequestParam @Min(0) int limit,
                                 @RequestParam String location, @RequestParam String major, @RequestParam String[] keywords);

    @GetMapping(path = "/resumes")
    GenericMatchingResponse getMatchingByResume(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authz, @RequestParam int offset, @RequestParam @Min(0) int limit,
    @RequestParam @NotBlank String resumeId);

    @PutMapping(path = "/matchings")
    GenericMatchingResponse updateMatching(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authz, @RequestBody @Valid MatchingDto matchingDto);

    @PatchMapping(path = "/matchings/{matchingId}/resumes")
    GenericMatchingResponse updateMatchingByResume(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authz, @RequestParam @NotBlank String matchingId, @RequestParam @NotBlank String resumeId, @RequestParam @NotBlank String userId);

    @PatchMapping(path = "/matchings/{matchingId}/inactives")
    BaseResponse deleteMatching(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authz, @RequestParam @NotBlank String matchingId);

}
