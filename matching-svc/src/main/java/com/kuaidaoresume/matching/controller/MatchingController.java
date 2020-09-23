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
import java.util.Optional;

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
        return new JobListResponse(new JobList(matchedJobs, offset, limit));
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
        return new ResumeListResponse(new ResumeList(resumeDtos, page, pageSize));
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
        Optional<JobDto> jobDtoOptional = matchingService.getResumeTailoredJob(resumeUuid);
        if (jobDtoOptional.isPresent()) {
            return new GenericJobResponse(jobDtoOptional.get());
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
        return new ResumeListResponse(new ResumeList(resumeDtos, offset, limit));
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
        return new JobListResponse(new JobList(bookmarkedJobs, offset, limit));
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
        return new ResumeListResponse(new ResumeList(bookmarkedResumes, offset, limit));
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
        return new JobListResponse(new JobList(visitedJobs, offset, limit));
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
        return new ResumeListResponse(new ResumeList(visitedResumes, offset, limit));
    }

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @GetMapping("/jobs/search")
    public JobListResponse searchJobs(
        @RequestParam String country,
        @RequestParam String city,
        @RequestParam String term) {

        Collection<JobDto> jobs = matchingService.searchJobs(country, city, term);
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
        @RequestParam String country,
        @RequestParam String city,
        @RequestParam String term,
        @RequestParam @Min(0) int page,
        @RequestParam @Min(1) int pageSize) {

        Collection<JobDto> jobs = matchingService.searchJobs(country, city, term, page, pageSize);
        return new JobListResponse(new JobList(jobs, page, pageSize));
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
}
