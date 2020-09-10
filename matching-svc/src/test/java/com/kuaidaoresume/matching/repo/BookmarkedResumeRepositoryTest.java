package com.kuaidaoresume.matching.repo;

import com.kuaidaoresume.matching.model.BookmarkedResume;
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
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataMongoTest
public class BookmarkedResumeRepositoryTest {

    private static final String RESUME_UUID = "resume uuid";
    private static final String JOB_UUID = "job uuid";
    private static final String COMPANY_NAME = "company name";
    private static final LocalDateTime POST_DATE = LocalDateTime.of(2020, 8, 1, 0, 0);

    @Autowired
    private BookmarkedResumeRepository bookmarkedResumeRepository;

    @Autowired
    private JobRepository jobRepository;

    private BookmarkedResume bookmarkedResume;

    private Job job1;
    private Job job2;
    private Job job3;

    @BeforeEach
    public void setup() {
        bookmarkedResumeRepository.deleteAll();
        jobRepository.deleteAll();

        Location location = Location.builder()
            .country("Canada")
            .state("BC")
            .city("Vancouver")
            .build();

        job1 = jobRepository.save(Job.builder()
            .jobUuid(JOB_UUID + "1")
            .companyName(COMPANY_NAME)
            .location(location)
            .postDate(POST_DATE)
            .createdAt(Instant.now())
            .isActive(true)
            .build());
        job2 = jobRepository.save(Job.builder()
            .jobUuid(JOB_UUID + "2")
            .companyName(COMPANY_NAME)
            .location(location)
            .postDate(POST_DATE)
            .createdAt(Instant.now())
            .isActive(true)
            .build());
        job3 = jobRepository.save(Job.builder()
            .jobUuid(JOB_UUID + "3")
            .companyName(COMPANY_NAME)
            .location(location)
            .postDate(POST_DATE)
            .createdAt(Instant.now())
            .isActive(true)
            .build());

        bookmarkedResume = bookmarkedResumeRepository.save(BookmarkedResume.builder()
            .resumeUuid(RESUME_UUID)
            .bookmarkedJobs(Arrays.asList(job1, job2, job3))
            .build()
        );
    }

    @Test
    public void test_findByResumeUuid() {
        Optional<BookmarkedResume> bookmarkedResumeOptional = bookmarkedResumeRepository.findByResumeUuid(RESUME_UUID);
        assertTrue(bookmarkedResumeOptional.isPresent());
        assertThat(bookmarkedResumeOptional.get(), is(bookmarkedResume));
    }

    @Test
    public void test_findBookmarkedJobsWithLimit() {
        assertThat(bookmarkedResumeRepository.findBookmarkedJobs(RESUME_UUID, 0, 2),
            is(Arrays.asList(job1, job2)));
    }

    @Test
    public void test_findBookmarkedJobsOffLimit() {
        assertThat(bookmarkedResumeRepository.findBookmarkedJobs(RESUME_UUID, 0, 4),
            is(Arrays.asList(job1, job2, job3)));
    }

    @Test
    public void test_findBookmarkedJobsWithOffset() {
        assertThat(bookmarkedResumeRepository.findBookmarkedJobs(RESUME_UUID, 1, 2),
            is(Arrays.asList(job2, job3)));
    }

    @Test
    public void test_findBookmarkedJobsExceedOffset() {
        assertThat(bookmarkedResumeRepository.findBookmarkedJobs(RESUME_UUID, 3, 2),
            is(Lists.emptyList()));
    }
}