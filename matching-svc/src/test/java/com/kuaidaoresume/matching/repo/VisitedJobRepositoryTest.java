package com.kuaidaoresume.matching.repo;

import com.kuaidaoresume.matching.model.Location;
import com.kuaidaoresume.matching.model.Resume;
import com.kuaidaoresume.matching.model.VisitedJob;
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
public class VisitedJobRepositoryTest {

    private static final String JOB_UUID = "job uuid";
    private static final String RESUME_UUID = "resume uuid";
    private static final String USER_ID = "user id";

    @Autowired
    private VisitedJobRepository visitedJobRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    private VisitedJob visitedJob;

    private Resume resume1;
    private Resume resume2;
    private Resume resume3;

    @BeforeEach
    public void setup() {
        visitedJobRepository.deleteAll();
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

        visitedJob = visitedJobRepository.save(VisitedJob.builder()
            .jobUuid(JOB_UUID)
            .visitedResumes(Arrays.asList(resume1, resume2, resume3))
            .build()
        );
    }

    @Test
    public void test_findByJobUuid() {
        Optional<VisitedJob> visitedJobOptional = visitedJobRepository.findByJobUuid(JOB_UUID);
        assertTrue(visitedJobOptional.isPresent());
        assertThat(visitedJobOptional.get(), is(visitedJob));
    }

    @Test
    public void test_findVisitedResumesWithLimit() {
        assertThat(visitedJobRepository.findVisitedResumes(JOB_UUID, 1 ,2),
            is(Arrays.asList(resume2, resume3)));
    }

    @Test
    public void test_findVisitedResumesOffLimit() {
        assertThat(visitedJobRepository.findVisitedResumes(JOB_UUID, 0 ,4),
            is(Arrays.asList(resume1, resume2, resume3)));
    }

    @Test
    public void test_findVisitedResumesWithOffset() {
        assertThat(visitedJobRepository.findVisitedResumes(JOB_UUID, 2 ,1),
            is(Arrays.asList(resume3)));
    }

    @Test
    public void test_findVisitedResumesExceedOffset() {
        assertThat(visitedJobRepository.findVisitedResumes(JOB_UUID, 3 ,1),
            is(Lists.emptyList()));
    }
}
