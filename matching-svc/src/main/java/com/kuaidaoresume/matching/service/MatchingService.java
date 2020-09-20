package com.kuaidaoresume.matching.service;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import com.google.common.collect.Lists;
import com.kuaidaoresume.common.api.ResultCode;
import com.kuaidaoresume.common.error.ServiceException;
import com.kuaidaoresume.matching.dto.JobDto;
import com.kuaidaoresume.matching.dto.KeywordDto;
import com.kuaidaoresume.matching.dto.LocationDto;
import com.kuaidaoresume.matching.dto.ResumeDto;
import com.kuaidaoresume.matching.dto.ResumeJobScoreDto;
import com.kuaidaoresume.matching.score.rules.IScoreRule;
import com.kuaidaoresume.matching.score.rules.KeywordScoreRule;
import com.kuaidaoresume.matching.score.rules.MajorScoreRule;
import com.kuaidaoresume.matching.model.*;
import com.kuaidaoresume.matching.repo.*;
import com.kuaidaoresume.matching.service.helper.ServiceHelper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
    private final BookmarkedResumeRepository bookmarkedResumeRepository;

    @Autowired
    private final BookmarkedJobRepository bookmarkedJobRepository;

    @Autowired
    private final VisitedResumeRepository visitedResumeRepository;

    @Autowired
    private final VisitedJobRepository visitedJobRepository;

    @Autowired
    private final ModelMapper modelMapper;

    @Autowired
    private final ServiceHelper serviceHelper;

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
            Collection<String> majors = resumeDto.getMajors() != null ? resumeDto.getMajors() : Lists.newArrayList();
            Collection<String> keywords = resumeDto.getKeywords() != null ? resumeDto.getKeywords() : Lists.newArrayList();
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
        Collection<String> relevantMajors = jobDto.getRelevantMajors() != null ? jobDto.getRelevantMajors() : Lists.newArrayList();
        Collection<String> keywords = jobDto.getKeywords() != null ?
            jobDto.getKeywords().stream().map(KeywordDto::getValue).collect(Collectors.toSet()) : Lists.newArrayList();
        List<Resume> matchedResumes = resumeRepository.findMatchedResumes(location.getCountry(), location.getCity(),
            relevantMajors, keywords);
        return matchedResumes.stream().map(resume -> modelMapper.map(resume, ResumeDto.class)).collect(Collectors.toList());
    }

    public Collection<ResumeDto> findMatchedResumes(JobDto jobDto, int page, int pageSize) {
        LocationDto location = jobDto.getLocation();
        Collection<String> relevantMajors = jobDto.getRelevantMajors() != null ? jobDto.getRelevantMajors() : Lists.newArrayList();
        Collection<String> keywords = jobDto.getKeywords() != null ?
            jobDto.getKeywords().stream().map(KeywordDto::getValue).collect(Collectors.toSet()) : Lists.newArrayList();
        List<Resume> matchedResumes = resumeRepository.findMatchedResumes(location.getCountry(), location.getCity(),
            relevantMajors, keywords, page, pageSize);
        return matchedResumes.stream().map(resume -> modelMapper.map(resume, ResumeDto.class)).collect(Collectors.toList());
    }

    @Transactional
    public void addTailoredResume(String resumeUuid, String jobUuid, boolean addBookmark) {
        Resume resume = getResumeByUuid(resumeUuid);
        Job job = getJobByUuid(jobUuid);

        TailoredJob tailoredJob = buildTailoredJob(jobUuid, resume);
        tailoredJobRepository.save(tailoredJob);

        TailoredResume tailoredResume = buildTailoredResume(resumeUuid, job);
        tailoredResumeRepository.save(tailoredResume);

        if (addBookmark) {
            bookmarkJobAsync(resumeUuid, jobUuid, resume, job);
        }
    }

    private void bookmarkJobAsync(String resumeUuid, String jobUuid, Resume resume, Job job) {
        serviceHelper.executeAsync(() -> {
            bookmarkJob(resumeUuid, jobUuid, resume, job);
        });
    }

    private Job getJobByUuid(String jobUuid) {
        return jobRepository.findByJobUuid(jobUuid).orElseThrow(() ->
            new ServiceException(ResultCode.NOT_FOUND, String.format("Job Not Found with uuid %s", jobUuid)));
    }

    private Resume getResumeByUuid(String resumeUuid) {
        return resumeRepository.findByResumeUuid(resumeUuid).orElseThrow(() ->
            new ServiceException(ResultCode.NOT_FOUND, String.format("Resume Not Found with uuid %s", resumeUuid)));
    }

    private TailoredJob buildTailoredJob(String jobUuid, Resume resume) {
        Set<Resume> resumesSet = new TreeSet<>(Comparator.comparing(Resume::getResumeUuid));
        TailoredJob tailoredJob = tailoredJobRepository.findByJobUuid(jobUuid).orElse(TailoredJob.builder()
            .jobUuid(jobUuid)
            .tailoredResumes(Lists.newArrayList())
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

    public Optional<JobDto> getResumeTailoredJob(String resumeUuid) {
        return tailoredResumeRepository.findByResumeUuid(resumeUuid).map(tailoredResume ->
            modelMapper.map(tailoredResume.getTargetJob(), JobDto.class));
    }

    public Collection<ResumeDto> getTailoredResumesByJob(String jobUuid) {
        TailoredJob tailoredJob = tailoredJobRepository.findByJobUuid(jobUuid).orElse(TailoredJob.builder()
            .tailoredResumes(Lists.newArrayList())
            .build()
        );
        return tailoredJob.getTailoredResumes().stream()
            .map(resume -> modelMapper.map(resume, ResumeDto.class)).collect(Collectors.toList());
    }

    public Collection<ResumeDto> getTailoredResumesByJob(String jobUuid, int offset, int limit) {
        Collection<Resume> tailoredResumes = tailoredJobRepository.findTailoredResumesWithLimit(jobUuid, offset, limit);
        return tailoredResumes.stream().map(resume -> modelMapper.map(resume, ResumeDto.class)).collect(Collectors.toList());
    }

    @Transactional
    public void bookmarkJob(String resumeUuid, String jobUuid) {
        Resume resume = getResumeByUuid(resumeUuid);
        Job job = getJobByUuid(jobUuid);

        bookmarkJob(resumeUuid, jobUuid, resume, job);
    }

    private void bookmarkJob(String resumeUuid, String jobUuid, Resume resume, Job job) {
        BookmarkedResume bookmarkedResume = buildBookmarkedResume(resumeUuid, job);
        bookmarkedResumeRepository.save(bookmarkedResume);

        BookmarkedJob bookmarkedJob = buildBookmarkedJob(jobUuid, resume);
        bookmarkedJobRepository.save(bookmarkedJob);
    }

    private BookmarkedResume buildBookmarkedResume(String resumeUuid, Job job) {
        Set<Job> jobsSet = new TreeSet<>(Comparator.comparing(Job::getJobUuid));
        BookmarkedResume bookmarkedResume = bookmarkedResumeRepository.findByResumeUuid(resumeUuid).orElse(
            BookmarkedResume.builder()
                .resumeUuid(resumeUuid)
                .bookmarkedJobs(Lists.newArrayList())
                .build());
        jobsSet.addAll(bookmarkedResume.getBookmarkedJobs());
        jobsSet.add(job);
        bookmarkedResume.setBookmarkedJobs(jobsSet);
        return bookmarkedResume;
    }

    private BookmarkedJob buildBookmarkedJob(String jobUuid, Resume resume) {
        Set<Resume> resumesSet = new TreeSet<>(Comparator.comparing(Resume::getResumeUuid));
        BookmarkedJob bookmarkedJob = bookmarkedJobRepository.findByJobUuid(jobUuid).orElse(BookmarkedJob.builder()
            .jobUuid(jobUuid)
            .bookmarkedResumes(Lists.newArrayList())
            .build());
        resumesSet.addAll(bookmarkedJob.getBookmarkedResumes());
        resumesSet.add(resume);
        bookmarkedJob.setBookmarkedResumes(resumesSet);
        return bookmarkedJob;
    }

    @Transactional
    public void markJobVisited(String resumeUuid, String jobUuid) {
        Resume resume = getResumeByUuid(resumeUuid);
        Job job = getJobByUuid(jobUuid);

        VisitedResume visitedResume = buildVisitedResume(resumeUuid, job);
        visitedResumeRepository.save(visitedResume);

        VisitedJob visitedJob = buildVisitedJob(jobUuid, resume);
        visitedJobRepository.save(visitedJob);
    }

    private VisitedResume buildVisitedResume(String resumeUuid, Job job) {
        Set<Job> jobsSet = new TreeSet<>(Comparator.comparing(Job::getJobUuid));
        VisitedResume visitedResume = visitedResumeRepository.findByResumeUuid(resumeUuid).orElse(
            VisitedResume.builder()
                .resumeUuid(resumeUuid)
                .visitedJobs(Lists.newArrayList())
                .build()
        );
        jobsSet.addAll(visitedResume.getVisitedJobs());
        jobsSet.add(job);
        visitedResume.setVisitedJobs(jobsSet);
        return visitedResume;
    }

    private VisitedJob buildVisitedJob(String jobUuid, Resume resume) {
        Set<Resume> resumesSet = new TreeSet<>(Comparator.comparing(Resume::getResumeUuid));
        VisitedJob visitedJob = visitedJobRepository.findByJobUuid(jobUuid).orElse(VisitedJob.builder()
            .jobUuid(jobUuid)
            .visitedResumes(Lists.newArrayList())
            .build()
        );
        resumesSet.addAll(visitedJob.getVisitedResumes());
        resumesSet.add(resume);
        visitedJob.setVisitedResumes(resumesSet);
        return visitedJob;
    }

    public Collection<JobDto> getResumeBookmarkedJobs(String resumeUuid) {
        BookmarkedResume bookmarkedResume = bookmarkedResumeRepository.findByResumeUuid(resumeUuid).orElse(
            BookmarkedResume.builder().bookmarkedJobs(Lists.newArrayList()).build()
        );
        return bookmarkedResume.getBookmarkedJobs().stream().map(job -> modelMapper.map(job, JobDto.class))
            .collect(Collectors.toList());
    }

    public Collection<JobDto> getResumeBookmarkedJobs(String resumeUuid, int offset, int limit) {
        return bookmarkedResumeRepository.findBookmarkedJobs(resumeUuid, offset, limit).stream().map(job ->
            modelMapper.map(job, JobDto.class)).collect(Collectors.toList());
    }

    public Collection<ResumeDto> getResumesBookmarkedByJob(String jobUuid) {
        BookmarkedJob bookmarkedJob = bookmarkedJobRepository.findByJobUuid(jobUuid).orElse(
            BookmarkedJob.builder().bookmarkedResumes(Lists.newArrayList()).build());
        return bookmarkedJob.getBookmarkedResumes().stream().map(resume -> modelMapper.map(resume, ResumeDto.class))
            .collect(Collectors.toList());
    }

    public Collection<ResumeDto> getResumesBookmarkedByJob(String jobUuid, int offset, int limit) {
        return bookmarkedJobRepository.findResumesBookmarkedByJob(jobUuid, offset, limit).stream().map(resume ->
            modelMapper.map(resume, ResumeDto.class)).collect(Collectors.toList());
    }

    public Collection<JobDto> getResumeVisitedJobs(String resumeUuid) {
        VisitedResume visitedResume = visitedResumeRepository.findByResumeUuid(resumeUuid).orElse(
            VisitedResume.builder().visitedJobs(Lists.newArrayList()).build()
        );
        return visitedResume.getVisitedJobs().stream().map(job -> modelMapper.map(job, JobDto.class))
            .collect(Collectors.toList());
    }

    public Collection<JobDto> getResumeVisitedJobs(String resumeUuid, int offset, int limit) {
        return visitedResumeRepository.findVisitedJobs(resumeUuid, offset, limit).stream().map(job ->
            modelMapper.map(job, JobDto.class)).collect(Collectors.toList());
    }

    public Collection<ResumeDto> getResumesVisitedByJob(String jobUuid) {
        VisitedJob visitedJob = visitedJobRepository.findByJobUuid(jobUuid).orElse(
            VisitedJob.builder().visitedResumes(Lists.newArrayList()).build());
        return visitedJob.getVisitedResumes().stream().map(resume -> modelMapper.map(resume, ResumeDto.class))
            .collect(Collectors.toList());
    }

    public Collection<ResumeDto> getResumesVisitedByJob(String jobUuid, int offset, int limit) {
        return visitedJobRepository.findVisitedResumes(jobUuid, offset, limit).stream().map(resume ->
            modelMapper.map(resume, ResumeDto.class)).collect(Collectors.toList());
    }

    public Collection<JobDto> searchJobs(String country, String city, String term) {
        return jobRepository.searchJobs(country, city, term).stream().map(job ->
            modelMapper.map(job, JobDto.class)).collect(Collectors.toList());
    }

    public Collection<JobDto> searchJobs(String country, String city, String term, int page, int pageSize) {
        return jobRepository.searchJobs(country, city, term, page, pageSize).stream().map(job ->
            modelMapper.map(job, JobDto.class)).collect(Collectors.toList());
    }

    public Collection<ResumeJobScoreDto> getResumeJobScore(String jobUuid, String resumeUuid) {
        Job job = getJobByUuid(jobUuid);
        Resume resume = getResumeByUuid(resumeUuid);
        Properties properties = new Properties();

        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("score/scores.properties")) {
            properties.load(new InputStreamReader(in, "UTF-8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Collection<IScoreRule> rules = new ArrayList<>();
        rules.add(new KeywordScoreRule());
        rules.add(new MajorScoreRule());

        return rules.stream().map(rule -> rule.score(properties, job, resume)).collect(Collectors.toList());
    }
}
