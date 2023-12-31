package com.kuaidaoresume.matching.service;

import com.kuaidaoresume.common.error.ServiceException;
import com.kuaidaoresume.matching.dto.LocationDto;
import com.kuaidaoresume.matching.dto.ResumeDto;
import com.kuaidaoresume.matching.model.*;
import com.kuaidaoresume.matching.repo.*;
import com.kuaidaoresume.matching.service.helper.ChineseHelper;
import com.kuaidaoresume.matching.service.helper.ServiceHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.cache.CacheManager;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MatchingServiceTest {

    private static final String JOB_UUID = "job uuid";
    private static final String RESUME_UUID = "resume uuid";
    private static final String COUNTRY = "Canada";
    private static final String CITY = "Vancouver";
    private static final String MAJOR = "aMajor";
    private static final String KEYWORD = "aKeyword";

    private MatchingService matchingService;

    private Instant now;
    private Instant earlier;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private ResumeRepository resumeRepository;

    @Mock
    private MatchedResumeRepository matchedResumeRepository;

    @Mock
    private TailoredResumeRepository tailoredResumeRepository;

    @Mock
    private TailoredJobRepository tailoredJobRepository;

    @Mock
    private BookmarkedResumeRepository bookmarkedResumeRepository;

    @Mock
    private BookmarkedJobRepository bookmarkedJobRepository;

    @Mock
    private VisitedResumeRepository visitedResumeRepository;

    @Mock
    private VisitedJobRepository visitedJobRepository;

    @Mock
    private MatchedResume matchedResume;

    @Mock
    private Job job;

    @Mock
    private Resume resume;

    @Mock
    private ResumeDto resumeDto;

    @Mock
    private LocationDto locationDto;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ServiceHelper serviceHelper;

    @Mock
    private ChineseHelper chineseHelper;

    @Mock
    private CacheManager cacheManager;

    @BeforeEach
    public void setup() {
        now = Instant.now();
        earlier = now.minusSeconds(1L);

        matchingService = new MatchingService(jobRepository, resumeRepository, matchedResumeRepository,
            tailoredResumeRepository, tailoredJobRepository, bookmarkedResumeRepository, bookmarkedJobRepository,
            visitedResumeRepository, visitedJobRepository, modelMapper, serviceHelper, chineseHelper, cacheManager);
    }

    private void setupMatchedResumeMocks() {
        when(resumeDto.getResumeUuid()).thenReturn(RESUME_UUID);
        when(matchedResumeRepository.findByResumeUuid(RESUME_UUID)).thenReturn(Optional.of(matchedResume));
        when(jobRepository.findTopByOrderByCreatedAtDesc()).thenReturn(Optional.of(job));
    }

    @Test
    public void test_findMatchedJobsShouldNotRematchWhenLastMatchIsLaterThanLastJobAdded() {
        setupMatchedResumeMocks();
        when(matchedResume.getLastMatchedAt()).thenReturn(now);
        when(job.getCreatedAt()).thenReturn(earlier);

        matchingService.findMatchedJobs(resumeDto);

        verify(jobRepository, never()).findMatchedJobs(anyString(), anyString(), anyList(), anyList());
    }

    @Test
    public void test_findMatchedJobsShouldMatchWhenLastJobAddedIsLaterThanLastMatched() {
        setupMatchedResumeMocks();
        when(locationDto.getCountry()).thenReturn(COUNTRY);
        when(locationDto.getCity()).thenReturn(CITY);
        when(resumeDto.getLocation()).thenReturn(locationDto);
        when(resumeDto.getMajors()).thenReturn(Arrays.asList(MAJOR));
        when(resumeDto.getKeywords()).thenReturn(Arrays.asList(KEYWORD));
        when(jobRepository.findMatchedJobs(COUNTRY, CITY, Arrays.asList(MAJOR), Arrays.asList(KEYWORD))).thenReturn(Arrays.asList(job));

        when(matchedResume.getLastMatchedAt()).thenReturn(earlier);
        when(job.getCreatedAt()).thenReturn(now);

        matchingService.findMatchedJobs(resumeDto);

        verify(jobRepository).findMatchedJobs(COUNTRY, CITY, Arrays.asList(MAJOR), Arrays.asList(KEYWORD));
    }

    @Test
    public void test_findMatchedJobsShouldMatchWhenNoPreviousMatch() {
        setupMatchedResumeMocks();
        when(locationDto.getCountry()).thenReturn(COUNTRY);
        when(locationDto.getCity()).thenReturn(CITY);
        when(resumeDto.getLocation()).thenReturn(locationDto);
        when(resumeDto.getMajors()).thenReturn(Arrays.asList(MAJOR));
        when(resumeDto.getKeywords()).thenReturn(Arrays.asList(KEYWORD));
        when(jobRepository.findMatchedJobs(anyString(), anyString(), anyList(), anyList())).thenReturn(Arrays.asList(job));

        when(matchedResumeRepository.findByResumeUuid(RESUME_UUID)).thenReturn(Optional.empty());

        matchingService.findMatchedJobs(resumeDto);

        verify(jobRepository).findMatchedJobs(COUNTRY, CITY, Arrays.asList(MAJOR), Arrays.asList(KEYWORD));
    }

    @Test
    public void test_addTailoredResume() {
        when(resume.getResumeUuid()).thenReturn(RESUME_UUID);
        when(resumeRepository.findByResumeUuid(RESUME_UUID)).thenReturn(Optional.of(resume));
        when(jobRepository.findByJobUuid(JOB_UUID)).thenReturn(Optional.of(job));

        matchingService.addTailoredResume(RESUME_UUID, JOB_UUID, true);

        Collection<Resume> tailoredResumes = new TreeSet<>(Comparator.comparing(Resume::getResumeUuid));
        tailoredResumes.add(resume);
        verify(tailoredResumeRepository).save(TailoredResume.builder()
            .resumeUuid(RESUME_UUID)
            .targetJob(job)
            .build());
        verify(tailoredJobRepository).save(TailoredJob.builder()
            .jobUuid(JOB_UUID)
            .tailoredResumes(tailoredResumes)
            .build()
        );
    }

    @Test
    public void test_addTailoredResumeThrowMatchingServiceExceptionWhenResumeNotExists() {
        when(resumeRepository.findByResumeUuid(RESUME_UUID)).thenReturn(Optional.empty());
        assertThrows(ServiceException.class, () -> matchingService.addTailoredResume(RESUME_UUID, JOB_UUID, true));
    }

    @Test
    public void test_addTailoredResumeThrowMatchingServiceExceptionWhenJobNotExists() {
        when(resumeRepository.findByResumeUuid(RESUME_UUID)).thenReturn(Optional.of(resume));
        when(jobRepository.findByJobUuid(JOB_UUID)).thenReturn(Optional.empty());
        assertThrows(ServiceException.class, () -> matchingService.addTailoredResume(RESUME_UUID, JOB_UUID, true));
    }

    @Test
    public void test_bookmarkJob() {
        when(resume.getResumeUuid()).thenReturn(RESUME_UUID);
        when(job.getJobUuid()).thenReturn(JOB_UUID);
        when(resumeRepository.findByResumeUuid(RESUME_UUID)).thenReturn(Optional.of(resume));
        when(jobRepository.findByJobUuid(JOB_UUID)).thenReturn(Optional.of(job));

        matchingService.bookmarkJob(RESUME_UUID, JOB_UUID);

        verify(bookmarkedResumeRepository).save(any(BookmarkedResume.class));
        verify(bookmarkedJobRepository).save(any(BookmarkedJob.class));
    }

    @Test
    public void test_markJobVisited() {
        when(resume.getResumeUuid()).thenReturn(RESUME_UUID);
        when(job.getJobUuid()).thenReturn(JOB_UUID);
        when(resumeRepository.findByResumeUuid(RESUME_UUID)).thenReturn(Optional.of(resume));
        when(jobRepository.findByJobUuid(JOB_UUID)).thenReturn(Optional.of(job));

        matchingService.markJobVisited(RESUME_UUID, JOB_UUID);

        verify(visitedResumeRepository).save(any(VisitedResume.class));
        verify(visitedJobRepository).save(any(VisitedJob.class));
    }
}
