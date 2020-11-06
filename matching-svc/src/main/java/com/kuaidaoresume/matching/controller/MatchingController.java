package com.kuaidaoresume.matching.controller;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import com.kuaidaoresume.common.api.BaseResponse;
import com.kuaidaoresume.common.auth.AuthConstant;
import com.kuaidaoresume.common.auth.Authorize;
import com.kuaidaoresume.matching.dto.*;
import com.kuaidaoresume.matching.service.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.Objects;

@RestController
@RequestMapping("/v1/matching")
@Validated
public class MatchingController {

    static final ILogger logger = SLoggerFactory.getLogger(MatchingController.class);

    @Autowired
    private MatchingService matchingService;

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_JOB_SERVICE,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @PostMapping("/jobs")
    public BaseResponse addJob(@RequestBody @Valid JobDto jobDto) {
        matchingService.addJob(jobDto);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setMessage("job added");

        return baseResponse;
    }

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_RESUME_SERVICE,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @PostMapping("/resumes")
    public BaseResponse addResume(@RequestBody @Valid ResumeDto resumeDto) {
        matchingService.addResume(resumeDto);

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setMessage("resume added");

        return baseResponse;
    }

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @PostMapping("/jobs/match")
    public JobListResponse findMatchedJobs(@RequestBody @Valid ResumeDto resumeDto) {
        Collection<JobDto> matchedJobs = matchingService.findMatchedJobs(resumeDto);
        return new JobListResponse(new JobList(matchedJobs));
    }

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @PostMapping("/jobs/paging-match")
    public JobListResponse findMatchedJobs(@RequestBody @Valid ResumeDto resumeDto,
                                           @RequestParam @Min(0) int offset, @RequestParam @Min(1) int limit) {

        Collection<JobDto> matchedJobs = matchingService.findMatchedJobs(resumeDto, offset, limit);
        long total = matchingService.countMatchedJobs(resumeDto);
        return new JobListResponse(
            JobListWithPaging.builder().jobs(matchedJobs).offset(offset).limit(limit).total(total).build());
    }

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @PostMapping("/resumes/match")
    public ResumeListResponse findMatchedResumes(@RequestBody @Valid JobDto jobDto) {
        Collection<ResumeDto> resumeDtos = matchingService.findMatchedResumes(jobDto);
        return new ResumeListResponse(new ResumeList(resumeDtos));
    }

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @PostMapping("/resumes/paging-match")
    public ResumeListResponse findMatchedResumes(@RequestBody @Valid JobDto jobDto,
                                                 @RequestParam @Min(0) int page, @RequestParam  @Min(1) int pageSize) {

        Collection<ResumeDto> resumeDtos = matchingService.findMatchedResumes(jobDto, page, pageSize);
        long total = matchingService.countMatchedResumes(jobDto);
        return new ResumeListResponse(
            ResumeListWithPaging.builder().resumes(resumeDtos).offset(page).limit(pageSize).total(total).build());
    }

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @PostMapping("/resumes/tailor")
    public BaseResponse addTailoredResume(@RequestParam String resumeUuid, @RequestParam String jobUuid,
        @RequestParam boolean addBookmark) {

        matchingService.addTailoredResume(resumeUuid, jobUuid, addBookmark);

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setMessage("Tailored resume saved");

        return baseResponse;
    }

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @GetMapping("/jobs/tailored")
    public GenericJobResponse getTailoredJob(@RequestParam String resumeUuid) {
        JobDto tailoredJob = matchingService.getResumeTailoredJob(resumeUuid);
        if (Objects.nonNull(tailoredJob)) {
            return new GenericJobResponse(tailoredJob);
        } else {
            GenericJobResponse response = new GenericJobResponse();
            response.setMessage("No tailored job with resume " + resumeUuid);

            return response;
        }
    }

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @GetMapping("/resumes/tailored")
    public ResumeListResponse getTailoredResumes(@RequestParam String jobUuid) {
        Collection<ResumeDto> resumeDtos = matchingService.getTailoredResumesByJob(jobUuid);
        return new ResumeListResponse(new ResumeList(resumeDtos));
    }

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @GetMapping("/resumes/paging-tailored")
    public ResumeListResponse getTailoredResumes(
        @RequestParam String jobUuid,
        @RequestParam @Min(0) int offset,
        @RequestParam @Min(1) int limit) {

        Collection<ResumeDto> resumeDtos = matchingService.getTailoredResumesByJob(jobUuid, offset, limit);
        long total = matchingService.countTailoredResumesByJob(jobUuid);
        return new ResumeListResponse(
            ResumeListWithPaging.builder().resumes(resumeDtos).offset(offset).limit(limit).total(total).build());
    }

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @PostMapping("/resumes/bookmark")
    public BaseResponse bookmarkJob(@RequestParam String resumeUuid, @RequestParam String jobUuid) {
        matchingService.bookmarkJob(resumeUuid, jobUuid);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setMessage(String.format("Job %s is bookmarked by resume %s", jobUuid, resumeUuid));
        return baseResponse;
    }

    @Authorize(value = {
      AuthConstant.AUTHORIZATION_WWW_SERVICE,
      AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
      AuthConstant.AUTHORIZATION_SUPPORT_USER,
      AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @DeleteMapping("/resumes/bookmark")
    public BaseResponse unbookmarkJob(@RequestParam String resumeUuid, @RequestParam String jobUuid) {
        matchingService.unbookmarkJob(resumeUuid, jobUuid);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setMessage(String.format("Job %s is un-bookmarked by resume %s", jobUuid, resumeUuid));
        return baseResponse;
    }

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @GetMapping("/jobs/bookmarked")
    public JobListResponse getBookmarkedJobs(@RequestParam String resumeUuid) {
        Collection<JobDto> bookmarkedJobs = matchingService.getResumeBookmarkedJobs(resumeUuid);
        return new JobListResponse(new JobList(bookmarkedJobs));
    }

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @GetMapping("/jobs/paging-bookmarked")
    public JobListResponse getBookmarkedJobs(
        @RequestParam String resumeUuid,
        @RequestParam @Min(0) int offset,
        @RequestParam @Min(1) int limit) {

        Collection<JobDto> bookmarkedJobs = matchingService.getResumeBookmarkedJobs(resumeUuid, offset, limit);
        long total = matchingService.countResumeBookmarkedJobs(resumeUuid);
        return new JobListResponse(
            JobListWithPaging.builder().jobs(bookmarkedJobs).offset(offset).limit(limit).total(total).build());
    }

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @GetMapping("/resumes/bookmarked")
    public ResumeListResponse getBookmarkedResumes(@RequestParam String jobUuid) {
        Collection<ResumeDto> bookmarkedResumes = matchingService.getResumesBookmarkedByJob(jobUuid);
        return new ResumeListResponse(new ResumeList(bookmarkedResumes));
    }

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @GetMapping("/resumes/paging-bookmarked")
    public ResumeListResponse getBookmarkedResumes(
        @RequestParam String jobUuid,
        @RequestParam @Min(0) int offset,
        @RequestParam @Min(1) int limit) {

        Collection<ResumeDto> bookmarkedResumes = matchingService.getResumesBookmarkedByJob(jobUuid, offset, limit);
        long total = matchingService.countBookmarkedResumesByJob(jobUuid);
        return new ResumeListResponse(
            ResumeListWithPaging.builder().resumes(bookmarkedResumes).offset(offset).limit(limit).total(total).build());
    }

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @PostMapping("/resumes/visit")
    public BaseResponse visitJob(@RequestParam String resumeUuid, @RequestParam String jobUuid) {
        matchingService.markJobVisited(resumeUuid, jobUuid);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setMessage(String.format("Job %s is marked as visited by resume %s", jobUuid, resumeUuid));
        return baseResponse;
    }

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @GetMapping("/jobs/visited")
    public JobListResponse getVisitedJobs(@RequestParam String resumeUuid) {
        Collection<JobDto> visitedJobs = matchingService.getResumeVisitedJobs(resumeUuid);
        return new JobListResponse(new JobList(visitedJobs));
    }

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @GetMapping("/jobs/paging-visited")
    public JobListResponse getVisitedJobs(
        @RequestParam String resumeUuid,
        @RequestParam @Min(0) int offset,
        @RequestParam @Min(1) int limit) {

        Collection<JobDto> visitedJobs = matchingService.getResumeVisitedJobs(resumeUuid, offset, limit);
        long total = matchingService.countResumeVisitedJobs(resumeUuid);
        return new JobListResponse(
            JobListWithPaging.builder().jobs(visitedJobs).offset(offset).limit(limit).total(total).build());
    }

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @GetMapping("/resumes/visited")
    public ResumeListResponse getVisitedResumes(@RequestParam String jobUuid) {
        Collection<ResumeDto> visitedResumes = matchingService.getResumesVisitedByJob(jobUuid);
        return new ResumeListResponse(new ResumeList(visitedResumes));
    }

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @GetMapping("/resumes/paging-visited")
    public ResumeListResponse getVisitedResumes(
        @RequestParam String jobUuid,
        @RequestParam @Min(0) int offset,
        @RequestParam @Min(1) int limit) {

        Collection<ResumeDto> visitedResumes = matchingService.getResumesVisitedByJob(jobUuid, offset, limit);
        long total = matchingService.countVisitedResumesByJob(jobUuid);
        return new ResumeListResponse(
            ResumeListWithPaging.builder().resumes(visitedResumes).offset(offset).limit(limit).total(total).build());
    }

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @GetMapping("/jobs/search")
    public JobListResponse searchJobs(
        @RequestParam(required = false) String country,
        @RequestParam(required = false) String city,
        @RequestParam String term) {

        Collection<JobDto> jobs = matchingService.searchJobs(buildSearchJobDto(country, city, term));
        return new JobListResponse(new JobList(jobs));
    }

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @GetMapping("/jobs/paging-search")
    public JobListResponse searchJobs(
        @RequestParam(required = false) String country,
        @RequestParam(required = false) String city,
        @RequestParam String term,
        @RequestParam @Min(0) int page,
        @RequestParam @Min(1) int pageSize) {

        Collection<JobDto> jobs = matchingService.searchJobs(buildSearchJobDto(country, city, term), page, pageSize);
        long total = matchingService.countJobsMatchedByTerm(country, city, term);
        return new JobListResponse(
            JobListWithPaging.builder().jobs(jobs).offset(page).limit(pageSize).total(total).build());
    }

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @GetMapping("/resumes/score")
    public ResumeJobScoreResponse getResumeScore(
            @RequestParam String jobUuid, @RequestParam String resumeUuid) {
        Collection<ResumeJobScoreDto> resumeJobScores = matchingService.getResumeJobScore(jobUuid, resumeUuid);
        return new ResumeJobScoreResponse(resumeJobScores);
    }

    private SearchJobDto buildSearchJobDto(String country, String city, String term) {
        return SearchJobDto.builder()
            .country(country)
            .city(city)
            .term(term)
            .build();
    }
}
