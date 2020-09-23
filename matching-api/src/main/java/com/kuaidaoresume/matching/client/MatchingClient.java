package com.kuaidaoresume.matching.client;

import com.kuaidaoresume.common.api.BaseResponse;
import com.kuaidaoresume.common.auth.AuthConstant;
import com.kuaidaoresume.matching.MatchingConstant;
import com.kuaidaoresume.matching.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@FeignClient(name = MatchingConstant.SERVICE_NAME, path = "/v1/matching", url = "${kuaidaoresume.matching-service-endpoint}")
public interface MatchingClient {

    @PostMapping("/jobs")
    BaseResponse addJob(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authz,
        @RequestBody @Valid JobDto jobDto);

    @PostMapping("/resumes")
    BaseResponse addResume(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authz,
        @RequestBody @Valid ResumeDto resumeDto);

    @PostMapping("/jobs/match")
    JobListResponse findMatchedJobs(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authz,
        @RequestBody @Valid ResumeDto resumeDto);

    @PostMapping("/jobs/paging-match")
    JobListResponse findMatchedJobs(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authz,
        @RequestBody @Valid ResumeDto resumeDto,
        @RequestParam @Min(0) int offset, @RequestParam @Min(1) int limit);

    @PostMapping("/resumes/match")
    ResumeListResponse findMatchedResumes(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authz,
        @RequestBody @Valid JobDto jobDto);

    @PostMapping("/resumes/paging-match")
    ResumeListResponse findMatchedResumes(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authz,
        @RequestBody @Valid JobDto jobDto,
        @RequestParam @Min(0) int page, @RequestParam  @Min(1) int pageSize);

    @PostMapping("/resumes/tailor")
    BaseResponse addTailoredResume(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authz,
        @RequestParam String resumeUuid, @RequestParam String jobUuid,
        @RequestParam boolean addBookmark);

    @GetMapping("/jobs/tailored")
    GenericJobResponse getTailoredJob(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authz,
        @RequestParam String resumeUuid);

    @GetMapping("/resumes/tailored")
    ResumeListResponse getTailoredResumes(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authz,
        @RequestParam String jobUuid);

    @GetMapping("/resumes/paging-tailored")
    ResumeListResponse getTailoredResumes(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authz,
        @RequestParam String jobUuid,
        @RequestParam @Min(0) int offset,
        @RequestParam @Min(1) int limit);

    @PostMapping("/resumes/bookmark")
    BaseResponse bookmarkJob(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authz,
        @RequestParam String resumeUuid, @RequestParam String jobUuid);

    @GetMapping("/jobs/bookmarked")
    JobListResponse getBookmarkedJobs(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authz,
        @RequestParam String resumeUuid);

    @GetMapping("/jobs/paging-bookmarked")
    JobListResponse getBookmarkedJobs(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authz,
        @RequestParam String resumeUuid,
        @RequestParam @Min(0) int offset,
        @RequestParam @Min(1) int limit);

    @GetMapping("/resumes/bookmarked")
    ResumeListResponse getBookmarkedResumes(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authz,
        @RequestParam String jobUuid);

    @GetMapping("/resumes/paging-bookmarked")
    ResumeListResponse getBookmarkedResumes(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authz,
        @RequestParam String jobUuid,
        @RequestParam @Min(0) int offset,
        @RequestParam @Min(1) int limit);

    @PostMapping("/resumes/visit")
    BaseResponse visitJob(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authz,
        @RequestParam String resumeUuid,
        @RequestParam String jobUuid);

    @GetMapping("/jobs/visited")
    JobListResponse getVisitedJobs(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authz,
        @RequestParam String resumeUuid);

    @GetMapping("/jobs/paging-visited")
    JobListResponse getVisitedJobs(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authz,
        @RequestParam String resumeUuid,
        @RequestParam @Min(0) int offset,
        @RequestParam @Min(1) int limit);

    @GetMapping("/resumes/visited")
    ResumeListResponse getVisitedResumes(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authz,
        @RequestParam String jobUuid);

    @GetMapping("/resumes/paging-visited")
    ResumeListResponse getVisitedResumes(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authz,
        @RequestParam String jobUuid,
        @RequestParam @Min(0) int offset,
        @RequestParam @Min(1) int limit);

    @GetMapping("/jobs/search")
    JobListResponse searchJobs(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authz,
        @RequestParam String country,
        @RequestParam String city,
        @RequestParam String term);

    @GetMapping("/jobs/paging-search")
    JobListResponse searchJobs(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authz,
        @RequestParam String country,
        @RequestParam String city,
        @RequestParam String term,
        @RequestParam @Min(0) int page,
        @RequestParam @Min(1) int pageSize);

    @GetMapping("/resumes/score")
    ResumeJobScoreResponse getResumeScore(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authz,
        @RequestParam String jobUuid,
        @RequestParam String resumeUuid);
}
