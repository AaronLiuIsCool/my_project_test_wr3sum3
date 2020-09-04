package com.kuaidaoresume.matching.service;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import com.kuaidaoresume.common.api.ResultCode;
import com.kuaidaoresume.common.error.ServiceException;
import com.kuaidaoresume.matching.dto.JobDto;
import com.kuaidaoresume.matching.dto.LocationDto;
import com.kuaidaoresume.matching.dto.ResumeDto;
import com.kuaidaoresume.matching.model.*;
import com.kuaidaoresume.matching.repo.*;
import com.kuaidaoresume.matching.service.helper.ServiceHelper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchingService {

    static ILogger logger = SLoggerFactory.getLogger(MatchingService.class);

    @Autowired
    private final JobRepository jobRepository;

    @Autowired
    private final ResumeRepository resumeRepository;

    @Autowired
    private final MatchedResumeRepository matchedResumeRepository;

    @Autowired
    private final TailoredResumeRepository tailoredResumeRepository;

    @Autowired
    private final TailoredJobRepository tailoredJobRepository;

    @Autowired
    private final ModelMapper modelMapper;

    @Autowired
    private final ServiceHelper serviceHelper;

//    public String inactiveMatching(String matchingId) {
//        Optional<Matching> existingMatching = matchingRepo.findById(Long.parseLong(matchingId));
//        if (!existingMatching.isPresent()) {
//            throw new ServiceException(ResultCode.NOT_FOUND, "Matching not found");
//        }
//        Matching updatedMatching = null;
//        // TODO: After Matching repo setup from TAIL-130
//        // existingg matching query update.
//        try {
//            updatedMatching = matchingRepo.save(existingMatching.get());
//        } catch (Exception ex) {
//            String errMsg = "could not update the matchingDto";
//            serviceHelper.handleErrorAndThrowException(logger, ex, errMsg);
//        }
//        LogEntry auditLog = LogEntry.builder()
//                .currentUserId(AuthContext.getUserId())
//                .authorization(AuthContext.getAuthz())
//                .targetType("matching")
//                //.targetId(matchingToUpdate.getId())
//                //.matchingId(matchingToUpdate.getId())
//                .originalContents(existingMatching.toString())
//                .updatedContents(updatedMatching.toString())
//                .build();
//        logger.info("inactive matching", auditLog);
//        serviceHelper.trackEventAsync("matching_inactive");
//
//        return updatedMatching.getId();
//    }

    public void addJob(JobDto jobDto) {
        Job job = modelMapper.map(jobDto, Job.class);
        job.setActive(true);
        job.setCreatedAt(Instant.now());
        jobRepository.save(job);
    }

    public void addResume(ResumeDto resumeDto) {
        Resume resume = modelMapper.map(resumeDto, Resume.class);
        resumeRepository.save(resume);
    }

    public Collection<JobDto> findMatchedJobs(ResumeDto resumeDto) {
        Collection<Job> matchedJobs = getMatchedJobs(resumeDto);
        return matchedJobs.stream().map(job -> modelMapper.map(job, JobDto.class)).collect(Collectors.toList());
    }

    public Collection<JobDto> findMatchedJobs(ResumeDto resumeDto, int offset, int limit) {
        Collection<Job> matchedJobs = getMatchedJobs(resumeDto);
        return matchedJobs.stream().skip(offset).limit(limit)
            .map(job -> modelMapper.map(job, JobDto.class)).collect(Collectors.toList());
    }

    private Collection<Job> getMatchedJobs(ResumeDto resumeDto) {
        String resumeUuid = resumeDto.getResumeUuid();

        saveResumeAsync(resumeDto, resumeUuid);

        MatchedResume matchedResume = matchedResumeRepository.findByResumeUuid(resumeUuid).orElseGet(() ->
            MatchedResume.builder().resumeUuid(resumeUuid).build());
        Job lastAddedJob = jobRepository.findTopByOrderByCreatedAtDesc().orElse(Job.builder()
            .createdAt(Instant.now())
            .build());
        Instant lastMatchedAt = matchedResume.getLastMatchedAt();
        Collection<Job> matchedJobs;
        if (lastMatchedAt != null && lastMatchedAt.isAfter(lastAddedJob.getCreatedAt())) {
            matchedJobs = matchedResume.getMatchedJobs();
        } else {
            LocationDto resumeLocation = resumeDto.getLocation();
            Collection<String> majors = resumeDto.getMajors() != null ? resumeDto.getMajors() : new ArrayList<>();
            Collection<String> keywords = resumeDto.getKeywords() != null ? resumeDto.getKeywords() : new ArrayList<>();
            matchedJobs = jobRepository.findMatchedJobs(
                resumeLocation.getCountry(), resumeLocation.getCity(), majors, keywords);

            saveMatchedResumeAsync(matchedResume, matchedJobs);
        }
        return matchedJobs;
    }

    private void saveMatchedResumeAsync(MatchedResume matchedResume, Collection<Job> matchedJobs) {
        serviceHelper.executeAsync(() -> {
            matchedResume.setMatchedJobs(matchedJobs);
            matchedResume.setLastMatchedAt(Instant.now());
            matchedResumeRepository.save(matchedResume);
        });
    }

    private void saveResumeAsync(ResumeDto resumeDto, String resumeUuid) {
        serviceHelper.executeAsync(() -> {
            String resumeId = resumeRepository.findByResumeUuid(resumeUuid).orElse(new Resume()).getId();
            Resume resumeToSave = modelMapper.map(resumeDto, Resume.class);
            resumeToSave.setId(resumeId);
            resumeRepository.save(resumeToSave);
        });
    }

    public Collection<ResumeDto> findMatchedResumes(JobDto jobDto) {
        LocationDto location = jobDto.getLocation();
        List<Resume> matchedResumes = resumeRepository.findMatchedResumes(location.getCountry(), location.getCity(),
            jobDto.getRelevantMajors(), jobDto.getKeywords());
        return matchedResumes.stream().map(resume -> modelMapper.map(resume, ResumeDto.class)).collect(Collectors.toList());
    }

    public Collection<ResumeDto> findMatchedResumes(JobDto jobDto, int page, int pageSize) {
        LocationDto location = jobDto.getLocation();
        List<Resume> matchedResumes = resumeRepository.findMatchedResumes(location.getCountry(), location.getCity(),
            jobDto.getRelevantMajors(), jobDto.getKeywords(), page, pageSize);
        return matchedResumes.stream().map(resume -> modelMapper.map(resume, ResumeDto.class)).collect(Collectors.toList());
    }

    public void addTailoredResume(String resumeUuid, String jobUuid) {
        Resume resume = resumeRepository.findByResumeUuid(resumeUuid).orElseThrow(() ->
            new ServiceException(ResultCode.NOT_FOUND, String.format("Resume Not Found with uuid %s", resumeUuid)));
        Job job = jobRepository.findByJobUuid(jobUuid).orElseThrow(() ->
            new ServiceException(ResultCode.NOT_FOUND, String.format("Job Not Found with uuid %s", jobUuid)));

        TailoredJob tailoredJob = buildTailoredJob(jobUuid, resume);
        tailoredJobRepository.save(tailoredJob);

        TailoredResume tailoredResume = buildTailoredResume(resumeUuid, job);
        tailoredResumeRepository.save(tailoredResume);
    }

    private TailoredJob buildTailoredJob(String jobUuid, Resume resume) {
        Set<Resume> resumesSet = new TreeSet<>(Comparator.comparing(Resume::getResumeUuid));
        TailoredJob tailoredJob = tailoredJobRepository.findByJobUuid(jobUuid).orElse(TailoredJob.builder()
            .jobUuid(jobUuid)
            .tailoredResumes(new ArrayList<>())
            .build());
        resumesSet.addAll(tailoredJob.getTailoredResumes());
        resumesSet.add(resume);
        tailoredJob.setTailoredResumes(resumesSet);
        return tailoredJob;
    }

    private TailoredResume buildTailoredResume(String resumeUuid, Job job) {
        TailoredResume tailoredResume = tailoredResumeRepository.findByResumeUuid(resumeUuid).orElse(TailoredResume.builder()
            .resumeUuid(resumeUuid)
            .build());
        tailoredResume.setTargetJob(job);
        return tailoredResume;
    }

    public JobDto getResumeTailoredJob(String resumeUuid) {
        TailoredResume tailoredResume = tailoredResumeRepository.findByResumeUuid(resumeUuid).orElseThrow(() ->
            new ServiceException(ResultCode.NOT_FOUND, String.format("Resume %s has no tailored job", resumeUuid)));
        return modelMapper.map(tailoredResume.getTargetJob(), JobDto.class);
    }

    public Collection<ResumeDto> getTailoredResumesByJob(String jobUuid) {
        TailoredJob tailoredJob = tailoredJobRepository.findByJobUuid(jobUuid).orElseThrow(() ->
            new ServiceException(ResultCode.NOT_FOUND, String.format("Job %s has not been tailored by any resumes", jobUuid)));
        return tailoredJob.getTailoredResumes().stream()
            .map(resume -> modelMapper.map(resume, ResumeDto.class)).collect(Collectors.toList());
    }

    public Collection<ResumeDto> getTailoredResumesByJob(String jobUuid, int offset, int limit) {
        Collection<Resume> tailoredResumes = tailoredJobRepository.findTailoredResumesWithLimit(jobUuid, offset, limit);
        return tailoredResumes.stream().map(resume -> modelMapper.map(resume, ResumeDto.class)).collect(Collectors.toList());
    }
}
