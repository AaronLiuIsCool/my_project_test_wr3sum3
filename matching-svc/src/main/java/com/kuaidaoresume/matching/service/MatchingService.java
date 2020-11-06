package com.kuaidaoresume.matching.service;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import com.google.common.collect.Lists;
import com.kuaidaoresume.common.api.ResultCode;
import com.kuaidaoresume.common.auditlog.LogEntry;
import com.kuaidaoresume.common.auth.AuthContext;
import com.kuaidaoresume.common.error.ServiceException;
import com.kuaidaoresume.matching.dto.*;
import com.kuaidaoresume.matching.model.*;
import com.kuaidaoresume.matching.repo.*;
import com.kuaidaoresume.matching.score.rules.IScoreRule;
import com.kuaidaoresume.matching.score.rules.KeywordScoreRule;
import com.kuaidaoresume.matching.score.rules.MajorScoreRule;
import com.kuaidaoresume.matching.service.helper.ChineseHelper;
import com.kuaidaoresume.matching.service.helper.ServiceHelper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchingService {

    private static ILogger logger = SLoggerFactory.getLogger(MatchingService.class);

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

    @Autowired
    private final ChineseHelper chineseHelper;

    @Autowired
    private final CacheManager cacheManager;

    @CacheEvict(cacheNames = {"matchedJobs", "searchJobs"})
    public void addJob(JobDto jobDto) {
        Job job = modelMapper.map(jobDto, Job.class);
        String title = job.getTitle();
        if (chineseHelper.containsChinese(title)) {
            job.setTitleForSearch(chineseHelper.convertChineseForSearch(title));
        }
        String companyName = job.getCompanyName();
        if (chineseHelper.containsChinese(companyName)) {
            job.setCompanyNameForSearch(chineseHelper.convertChineseForSearch(companyName));
        }
        job.setActive(true);
        job.setCreatedAt(Instant.now());
        jobRepository.save(job);
    }

    public void addResume(ResumeDto resumeDto) {
        Resume resume = modelMapper.map(resumeDto, Resume.class);
        resumeRepository.save(resume);
    }

    @Cacheable("matchedJobs")
    public Collection<JobDto> findMatchedJobs(ResumeDto resumeDto) {
        return getMatchedJobs(resumeDto);
    }

    public Collection<JobDto> findMatchedJobs(ResumeDto resumeDto, int offset, int limit) {
        try {
            Collection<JobDto> matchedJobDtos = null;
            Cache matchedJobsCache = cacheManager.getCache("matchedJobs");
            Cache.ValueWrapper valueWrapper = matchedJobsCache.get(resumeDto);
            if (Objects.nonNull(valueWrapper)) {
                matchedJobDtos = (Collection<JobDto>) valueWrapper.get();
            } else {
                LogEntry auditLog = LogEntry.builder()
                    .authorization(AuthContext.getAuthz())
                    .currentUserId(AuthContext.getUserId())
                    .targetType("matchedJobs caching")
                    .targetId(resumeDto.getResumeUuid())
                    .updatedContents("Cache missed")
                    .build();
                logger.info("Find matched jobs", auditLog);
                matchedJobDtos = getMatchedJobs(resumeDto);
                matchedJobsCache.put(resumeDto, matchedJobDtos);
            }
            return matchedJobDtos.stream().skip(offset).limit(limit).collect(Collectors.toList());
        } catch (RuntimeException e) {
            serviceHelper.handleErrorAndThrowException(logger, e, "Failed to find matched jobs.");
            throw e;
        }
    }

    public long countMatchedJobs(ResumeDto resumeDto) {
        return getMatchedJobs(resumeDto).size();
    }

    private Collection<JobDto> getMatchedJobs(ResumeDto resumeDto) {
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
        return matchedJobs.stream().map(job ->
            modelMapper.map(job, JobDto.class)).collect(Collectors.toList());
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

    public long countMatchedResumes(JobDto jobDto) {
        LocationDto location = jobDto.getLocation();
        Collection<String> relevantMajors = jobDto.getRelevantMajors() != null ? jobDto.getRelevantMajors() : Lists.newArrayList();
        Collection<String> keywords = jobDto.getKeywords() != null ?
            jobDto.getKeywords().stream().map(KeywordDto::getValue).collect(Collectors.toSet()) : Lists.newArrayList();
        return resumeRepository.countMatchedResumes(location.getCountry(), location.getCity(), relevantMajors, keywords);
    }

    @Transactional
    @Caching(
        put = {@CachePut(cacheNames = "tailoredJob", key = "#resumeUuid")},
        evict = {@CacheEvict(cacheNames = "bookmarkedJobs", key = "#resumeUuid")})
    public JobDto addTailoredResume(String resumeUuid, String jobUuid, boolean addBookmark) {
        Resume resume = getResumeByUuid(resumeUuid);
        Job job = getJobByUuid(jobUuid);

        TailoredJob tailoredJob = buildTailoredJob(jobUuid, resume);
        tailoredJobRepository.save(tailoredJob);

        TailoredResume tailoredResume = buildTailoredResume(resumeUuid, job);
        tailoredResumeRepository.save(tailoredResume);

        if (addBookmark) {
            bookmarkJobAsync(resumeUuid, jobUuid, resume, job);
        }

        // added return type to populate the result that would be used by @Cacheable("tailoredJob")
        return modelMapper.map(job, JobDto.class);
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

    @Cacheable(cacheNames = "tailoredJob")
    public JobDto getResumeTailoredJob(String resumeUuid) {
        return tailoredResumeRepository.findByResumeUuid(resumeUuid).map(tailoredResume ->
            modelMapper.map(tailoredResume.getTargetJob(), JobDto.class)).orElse(null);
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

    public long countTailoredResumesByJob(String jobUuid) {
        return tailoredJobRepository.findByJobUuid(jobUuid)
            .map(tailoredJob -> tailoredJob.getTailoredResumes().size())
            .orElse(0);
    }

    @Transactional
    @CachePut(cacheNames = "bookmarkedJobs", key = "#resumeUuid")
    public Collection<JobDto> bookmarkJob(String resumeUuid, String jobUuid) {
        Resume resume = getResumeByUuid(resumeUuid);
        Job job = getJobByUuid(jobUuid);

        Collection<Job> bookmarkJobs = bookmarkJob(resumeUuid, jobUuid, resume, job);
        return bookmarkJobs.stream()
            .map(bookmarkedJob -> modelMapper.map(bookmarkedJob, JobDto.class)).collect(Collectors.toList());
    }

    private Collection<Job> bookmarkJob(String resumeUuid, String jobUuid, Resume resume, Job job) {
        BookmarkedResume bookmarkedResume = buildBookmarkedResume(resumeUuid, job);
        bookmarkedResumeRepository.save(bookmarkedResume);

        BookmarkedJob bookmarkedJob = buildBookmarkedJob(jobUuid, resume);
        bookmarkedJobRepository.save(bookmarkedJob);
        return bookmarkedResume.getBookmarkedJobs();
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

    @Cacheable("bookmarkedJobs")
    public Collection<JobDto> getResumeBookmarkedJobs(String resumeUuid) {
        BookmarkedResume bookmarkedResume = bookmarkedResumeRepository.findByResumeUuid(resumeUuid).orElse(
            BookmarkedResume.builder().bookmarkedJobs(Lists.newArrayList()).build()
        );
        return bookmarkedResume.getBookmarkedJobs().stream().map(job -> modelMapper.map(job, JobDto.class))
            .collect(Collectors.toList());
    }

    public Collection<JobDto> getResumeBookmarkedJobs(String resumeUuid, int offset, int limit) {
        Collection<JobDto> bookmarkedJobs = null;
        Cache bookmarkedJobsCache = cacheManager.getCache("bookmarkedJobs");
        Cache.ValueWrapper valueWrapper = bookmarkedJobsCache.get(resumeUuid);
        if (Objects.nonNull(valueWrapper)) {
            bookmarkedJobs = (Collection<JobDto>) valueWrapper.get();
        }
        if (Objects.nonNull(bookmarkedJobs)) {
            return bookmarkedJobs.stream().skip(offset).limit(limit).collect(Collectors.toList());
        } else {
            LogEntry auditLog = LogEntry.builder()
                .authorization(AuthContext.getAuthz())
                .currentUserId(AuthContext.getUserId())
                .targetType("bookmarkedJobs caching")
                .targetId(resumeUuid)
                .updatedContents("Cache missed")
                .build();
            logger.info("get resume bookmarked jobs", auditLog);
            return bookmarkedResumeRepository.findBookmarkedJobs(resumeUuid, offset, limit).stream().map(job ->
                modelMapper.map(job, JobDto.class)).collect(Collectors.toList());
        }
    }

    @Transactional
    @CacheEvict(cacheNames = "bookmarkedJobs", key = "#resumeUuid")
    public void unbookmarkJob(String resumeUuid, String jobUuid) {
        bookmarkedResumeRepository.findByResumeUuid(resumeUuid).ifPresent(bookmarkedResume -> {
            Collection<Job> bookmarkedJobs = bookmarkedResume.getBookmarkedJobs();
            bookmarkedJobs.stream()
                .filter(job -> job.getJobUuid().equals(jobUuid)).findFirst()
                .ifPresent(toRemove -> {
                    bookmarkedJobs.remove(toRemove);
                    bookmarkedResume.setBookmarkedJobs(bookmarkedJobs);
                    bookmarkedResumeRepository.save(bookmarkedResume);
                });
        });

        bookmarkedJobRepository.findByJobUuid(jobUuid).ifPresent(bookmarkedJob -> {
            Collection<Resume> bookmarkedResumes = bookmarkedJob.getBookmarkedResumes();
            bookmarkedResumes.stream()
                .filter(resume -> resume.getResumeUuid().equals(resumeUuid)).findFirst()
                .ifPresent(toRemove -> {
                    bookmarkedResumes.remove(toRemove);
                    bookmarkedJob.setBookmarkedResumes(bookmarkedResumes);
                    bookmarkedJobRepository.save(bookmarkedJob);
                });
        });
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

    public long countBookmarkedResumesByJob(String jobUuid) {
        return bookmarkedJobRepository.findByJobUuid(jobUuid)
            .map(bookmarkedJob -> bookmarkedJob.getBookmarkedResumes().size())
            .orElse(0);
    }

    public long countResumeBookmarkedJobs(String resumeUuid) {
        return bookmarkedResumeRepository.findByResumeUuid(resumeUuid)
            .map(bookmarkedResume -> bookmarkedResume.getBookmarkedJobs().size())
            .orElse(0);
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

    public long countResumeVisitedJobs(String resumeUuid) {
        return visitedResumeRepository.findByResumeUuid(resumeUuid)
            .map(visitedResume -> visitedResume.getVisitedJobs().size())
            .orElse(0);
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

    public long countVisitedResumesByJob(String jobUuid) {
        return visitedJobRepository.findByJobUuid(jobUuid).map(visitedJob -> visitedJob.getVisitedResumes().size())
            .orElse(0);
    }

    @Cacheable("searchJobs")
    public Collection<JobDto> searchJobs(SearchJobDto searchJobDto) {
        String searchTerm = convertTermForSearchIfNecessary(searchJobDto.getTerm());
        return jobRepository.searchJobs(searchJobDto.getCountry(), searchJobDto.getCity(), searchTerm)
            .stream().map(job -> modelMapper.map(job, JobDto.class)).collect(Collectors.toList());
    }

    private String convertTermForSearchIfNecessary(String term) {
        if (chineseHelper.containsChinese(term)) {
            term += " " + chineseHelper.convertChineseForSearch(term);
        }
        return term;
    }

    public Collection<JobDto> searchJobs(SearchJobDto searchJobDto, int page, int pageSize) {
        Collection<JobDto> searchedJobs = null;
        Cache searchJobsCache = cacheManager.getCache("searchJobs");
        Cache.ValueWrapper valueWrapper = searchJobsCache.get(searchJobDto);
        if (Objects.nonNull(valueWrapper)) {
            searchedJobs = (Collection<JobDto>) valueWrapper.get();
        }
        if (Objects.nonNull(searchedJobs)) {
            return searchedJobs.stream().skip(page).limit(pageSize).collect(Collectors.toList());
        } else {
            LogEntry auditLog = LogEntry.builder()
                .authorization(AuthContext.getAuthz())
                .currentUserId(AuthContext.getUserId())
                .targetType("searchJobs caching")
                .updatedContents("Cache missed")
                .build();
            logger.info("Search jobs", auditLog);

            String searchTerm = convertTermForSearchIfNecessary(searchJobDto.getTerm());
            return jobRepository.searchJobs(searchJobDto.getCountry(), searchJobDto.getCity(), searchTerm, page, pageSize)
                .stream().map(job -> modelMapper.map(job, JobDto.class)).collect(Collectors.toList());
        }
    }

    public long countJobsMatchedByTerm(String country, String city, String term) {
        return jobRepository.countJobsMatchedByTerm(country, city, term);
    }

    public Collection<ResumeJobScoreDto> getResumeJobScore(String jobUuid, String resumeUuid) {
        Job job = getJobByUuid(jobUuid);
        Resume resume = getResumeByUuid(resumeUuid);
        Properties properties = new Properties();

        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("score/scores.properties")) {
            properties.load(new InputStreamReader(in, "UTF-8"));
        } catch (IOException e) {
            serviceHelper.handleErrorAndThrowException(logger, e, "Failed to get resume job score.");
            throw new RuntimeException(e);
        }

        Collection<IScoreRule> rules = new ArrayList<>();
        rules.add(new KeywordScoreRule());
        rules.add(new MajorScoreRule());

        return rules.stream().map(rule -> rule.score(properties, job, resume)).collect(Collectors.toList());
    }
}
