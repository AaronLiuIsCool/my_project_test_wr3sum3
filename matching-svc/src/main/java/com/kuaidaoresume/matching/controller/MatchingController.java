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

@RestController
@RequestMapping("/v1/matching")
@Validated
public class MatchingController {

    static final ILogger logger = SLoggerFactory.getLogger(MatchingController.class);

    @Autowired
    private MatchingService matchingService;

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE,
        //AuthConstant.AUTHORIZATION_WHOAMI_SERVICE,
        //AuthConstant.AUTHORIZATION_BOT_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
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
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE,
        //AuthConstant.AUTHORIZATION_WHOAMI_SERVICE,
        //AuthConstant.AUTHORIZATION_BOT_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
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
        AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE,
        //AuthConstant.AUTHORIZATION_WHOAMI_SERVICE,
        //AuthConstant.AUTHORIZATION_BOT_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @GetMapping("/jobs/match")
    public JobListResponse findMatchedJobs(@RequestBody @Valid ResumeDto resumeDto) {
        Collection<JobDto> matchedJobs = matchingService.findMatchedJobs(resumeDto);
        return new JobListResponse(new JobList(matchedJobs));
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
    @GetMapping("/jobs/paging-match")
    public JobListResponse findMatchedJobs(@RequestBody @Valid ResumeDto resumeDto,
                                           @RequestParam @Min(0) int offset, @RequestParam @Min(1) int limit) {

        Collection<JobDto> matchedJobs = matchingService.findMatchedJobs(resumeDto, offset, limit);
        return new JobListResponse(new JobList(matchedJobs, offset, limit));
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
    @GetMapping("/resumes/match")
    public ResumeListResponse findMatchedResumes(@RequestBody @Valid JobDto jobDto) {
        Collection<ResumeDto> resumeDtos = matchingService.findMatchedResumes(jobDto);
        return new ResumeListResponse(new ResumeList(resumeDtos));
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
    @GetMapping("/resumes/paging-match")
    public ResumeListResponse findMatchedResumes(@RequestBody @Valid JobDto jobDto,
                                                 @RequestParam @Min(0) int page, @RequestParam  @Min(1) int pageSize) {

        Collection<ResumeDto> resumeDtos = matchingService.findMatchedResumes(jobDto, page, pageSize);
        return new ResumeListResponse(new ResumeList(resumeDtos, page, pageSize));
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
    @PostMapping("/resumes/tailor")
    public BaseResponse addTailoredResume(@RequestParam String resumeUuid, @RequestParam String jobUuid) {
        matchingService.addTailoredResume(resumeUuid, jobUuid);

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setMessage("Tailored resume saved");

        return baseResponse;
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
    @GetMapping("/jobs/tailored")
    public GenericJobResponse getTailoredJob(@RequestParam String resumeUuid) {
        JobDto job = matchingService.getResumeTailoredJob(resumeUuid);
        return new GenericJobResponse(job);
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
    @GetMapping("/resumes/tailored")
    public ResumeListResponse getTailoredResumes(@RequestParam String jobUuid) {
        Collection<ResumeDto> resumeDtos = matchingService.getTailoredResumesByJob(jobUuid);
        return new ResumeListResponse(new ResumeList(resumeDtos));
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
    @GetMapping("/resumes/paging-tailored")
    public ResumeListResponse getTailoredResumes(@RequestParam String jobUuid, @RequestParam int offset, @RequestParam int limit) {
        Collection<ResumeDto> resumeDtos = matchingService.getTailoredResumesByJob(jobUuid, offset, limit);
        return new ResumeListResponse(new ResumeList(resumeDtos, offset, limit));
    }
}
