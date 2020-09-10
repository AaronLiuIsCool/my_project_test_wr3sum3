package com.kuaidaoresume.matching.repo;

import com.kuaidaoresume.matching.model.Job;
import com.kuaidaoresume.matching.model.Location;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataMongoTest
public class JobRepositoryTest {

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
    private JobRepository jobRepository;

    private Location location;
    private Job job;

    @BeforeEach
    public void setup() {
        jobRepository.deleteAll();

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
    }

    @Test
    public void test_matchJobByMajor() {
        List<Job> matchedJobs = jobRepository.findMatchedJobs(
            location.getCountry(), location.getCity(), Arrays.asList(MAJOR_A, MAJOR_C, "another major"), Lists.emptyList());
        assertThat(matchedJobs.size(), is(1));
        assertThat(matchedJobs.get(0), is(job));
    }

    @Test
    public void test_matchJobByKeyword() {
        List<Job> matchedJobs = jobRepository.findMatchedJobs(
            location.getCountry(), location.getCity(), Lists.emptyList(), Arrays.asList(KEYWORD2, "another keyword"));
        assertThat(matchedJobs.size(), is(1));
        assertThat(matchedJobs.get(0), is(job));
    }

    @Test
    public void test_shouldNotMatchInactiveJob() {
        jobRepository.save(Job.builder()
            .jobUuid("job uuid 2")
            .companyName(COMPANY_NAME)
            .location(location)
            .relevantMajors(Arrays.asList(MAJOR_A, MAJOR_B, MAJOR_C))
            .keywords(Arrays.asList(KEYWORD1, KEYWORD2, KEYWORD3))
            .postDate(POST_DATE)
            .createdAt(Instant.now())
            .isActive(false)
            .build());
        List<Job> matchedJobs = jobRepository.findMatchedJobs(
            location.getCountry(), location.getCity(), Lists.emptyList(), Arrays.asList(KEYWORD2));
        assertThat(matchedJobs.size(), is(1));
        assertThat(matchedJobs.get(0), is(job));
    }

    @Test
    public void test_noMatchedJobs() {
        List<Job> matchedJobs = jobRepository.findMatchedJobs(
            location.getCountry(), location.getCity(), Arrays.asList("another major"), Arrays.asList("keyword4"));
        assertTrue(matchedJobs.isEmpty());
    }

    @Test
    public void test_shouldnMatchJobsSortByPostDateDesc() {
        Job job1 = jobRepository.save(Job.builder()
            .jobUuid("job uuid 1")
            .companyName(COMPANY_NAME)
            .location(location)
            .relevantMajors(Arrays.asList(MAJOR_A, MAJOR_B, MAJOR_C))
            .keywords(Arrays.asList(KEYWORD1, KEYWORD2, KEYWORD3))
            .postDate(LocalDateTime.of(2020, 7, 1, 0, 0))
            .createdAt(Instant.now())
            .isActive(true)
            .build());

        Job job2 = jobRepository.save(Job.builder()
            .jobUuid("job uuid 2")
            .companyName(COMPANY_NAME)
            .location(location)
            .relevantMajors(Arrays.asList(MAJOR_A, MAJOR_B, MAJOR_C))
            .keywords(Arrays.asList(KEYWORD1, KEYWORD2, KEYWORD3))
            .postDate(LocalDateTime.of(2020, 9, 1, 0, 0))
            .createdAt(Instant.now())
            .isActive(true)
            .build());
        List<Job> matchedJobs = jobRepository.findMatchedJobs(
            location.getCountry(), location.getCity(), Lists.emptyList(), Arrays.asList(KEYWORD1, KEYWORD2));

        assertThat(matchedJobs.size(), is(3));
        assertThat(matchedJobs.get(0), is(job2));
        assertThat(matchedJobs.get(1), is(job));
        assertThat(matchedJobs.get(2), is(job1));
    }

    @Test
    public void test_findTopByOrderCreatedAtDesc() {
        jobRepository.save(Job.builder()
            .jobUuid("job uuid 1")
            .companyName(COMPANY_NAME)
            .location(location)
            .relevantMajors(Arrays.asList(MAJOR_A, MAJOR_B, MAJOR_C))
            .keywords(Arrays.asList(KEYWORD1, KEYWORD2, KEYWORD3))
            .postDate(LocalDateTime.of(2020, 7, 1, 0, 0))
            .createdAt(Instant.ofEpochMilli(1598230435))
            .isActive(true)
            .build());

        jobRepository.save(Job.builder()
            .jobUuid("job uuid 2")
            .companyName(COMPANY_NAME)
            .location(location)
            .relevantMajors(Arrays.asList(MAJOR_A, MAJOR_B, MAJOR_C))
            .keywords(Arrays.asList(KEYWORD1, KEYWORD2, KEYWORD3))
            .postDate(LocalDateTime.of(2020, 9, 1, 0, 0))
            .createdAt(Instant.ofEpochMilli(1598230436))
            .isActive(true)
            .build());

        Job lastJob = jobRepository.findTopByOrderByCreatedAtDesc().get();

        assertThat(lastJob, is(job));
    }
}