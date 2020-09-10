package com.kuaidaoresume.matching.repo;

import com.kuaidaoresume.matching.model.BookmarkedJob;
import com.kuaidaoresume.matching.model.Location;
import com.kuaidaoresume.matching.model.Resume;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataMongoTest
public class BookmarkedJobRepositoryTest {

    private static final String JOB_UUID = "job uuid";
    private static final String RESUME_UUID = "resume uuid";
    private static final String USER_ID = "user id";

    @Autowired
    private BookmarkedJobRepository bookmarkedJobRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    private BookmarkedJob bookmarkedJob;

    private Resume resume1;
    private Resume resume2;
    private Resume resume3;

    @BeforeEach
    public void setup() {
        bookmarkedJobRepository.deleteAll();
        resumeRepository.deleteAll();

        Location location = Location.builder()
            .country("Canada")
            .state("BC")
            .city("Vancouver")
            .build();

        resume1 = resumeRepository.save(Resume.builder()
            .resumeUuid(RESUME_UUID + "1")
            .userId(USER_ID)
            .location(location)
            .build()
        );
        resume2 = resumeRepository.save(Resume.builder()
            .resumeUuid(RESUME_UUID + "2")
            .userId(USER_ID)
            .location(location)
            .build()
        );
        resume3 = resumeRepository.save(Resume.builder()
            .resumeUuid(RESUME_UUID + "3")
            .userId(USER_ID)
            .location(location)
            .build()
        );

        bookmarkedJob = bookmarkedJobRepository.save(BookmarkedJob.builder()
            .jobUuid(JOB_UUID)
            .bookmarkedResumes(Arrays.asList(resume1, resume2, resume3))
            .build()
        );
    }

    @Test
    public void test_findByJobUuid() {
        Optional<BookmarkedJob> bookmarkedJobOptional = bookmarkedJobRepository.findByJobUuid(JOB_UUID);
        assertTrue(bookmarkedJobOptional.isPresent());
        assertThat(bookmarkedJobOptional.get(), is(bookmarkedJob));
    }

    @Test
    public void test_findBookmarkedResumesWithLimit() {
        assertThat(bookmarkedJobRepository.findResumesBookmarkedByJob(JOB_UUID, 1 ,2),
            is(Arrays.asList(resume2, resume3)));
    }

    @Test
    public void test_findBookmarkedResumesOffLimit() {
        assertThat(bookmarkedJobRepository.findResumesBookmarkedByJob(JOB_UUID, 0 ,4),
            is(Arrays.asList(resume1, resume2, resume3)));
    }

    @Test
    public void test_findBookmarkedResumesWithOffset() {
        assertThat(bookmarkedJobRepository.findResumesBookmarkedByJob(JOB_UUID, 2 ,1),
            is(Arrays.asList(resume3)));
    }

    @Test
    public void test_findBookmarkedResumesExceedOffset() {
        assertThat(bookmarkedJobRepository.findResumesBookmarkedByJob(JOB_UUID, 3 ,1),
            is(Lists.emptyList()));
    }
}
