package com.kuaidaoresume.matching.repo;

import com.kuaidaoresume.matching.config.MatchingApplicationTestConfig;
import com.kuaidaoresume.matching.model.Job;
import com.kuaidaoresume.matching.model.Location;
import com.kuaidaoresume.matching.model.TailoredResume;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(value = {MatchingApplicationTestConfig.class})
public class TailoredResumeRepositoryTest {

    private static final String RESUME_UUID = "resume uuid";
    private static final String JOB_UUID = "job uuid";
    private static final String COMPANY_NAME = "company name";
    private static final String MAJOR_A = "major a";
    private static final String MAJOR_B = "major b";
    private static final String MAJOR_C = "major c";
    private static final String KEYWORD1 = "keyword1";
    private static final String KEYWORD2 = "keyword2";
    private static final String KEYWORD3 = "keyword3";
    private static final LocalDateTime POST_DATE = LocalDateTime.of(2020, 8, 1, 0, 0);

    @Autowired
    private TailoredResumeRepository tailoredResumeRepository;

    @Autowired
    private JobRepository jobRepository;

    private Location location;
    private Job job;
    private TailoredResume tailoredResume;

    @BeforeEach
    public void setup() {
        jobRepository.deleteAll();
        tailoredResumeRepository.deleteAll();

        location = Location.builder()
            .country("Canada")
            .state("BC")
            .city("Vancouver")
            .build();
        job = jobRepository.save(Job.builder()
            .jobUuid(JOB_UUID)
            .companyName(COMPANY_NAME)
            .location(location)
            .relevantMajors(Arrays.asList(MAJOR_A, MAJOR_B, MAJOR_C))
            .keywords(Arrays.asList(KEYWORD1, KEYWORD2, KEYWORD3))
            .postDate(POST_DATE)
            .createdAt(Instant.now())
            .isActive(true)
            .build());
        tailoredResume = tailoredResumeRepository.save(TailoredResume.builder()
            .resumeUuid(RESUME_UUID)
            .targetJob(job)
            .build());
    }

    @Test
    public void test_findByResumeUuid() {
        assertTrue(tailoredResumeRepository.findByResumeUuid(RESUME_UUID).isPresent());
        assertThat(tailoredResumeRepository.findByResumeUuid(RESUME_UUID).get(), is(tailoredResume));
    }

    @Test
    public void test_findByResumeUuidNotExists() {
        assertFalse(tailoredResumeRepository.findByResumeUuid("other uuid").isPresent());
    }
}