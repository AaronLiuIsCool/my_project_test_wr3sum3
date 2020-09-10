package com.kuaidaoresume.matching.repo;

import com.kuaidaoresume.matching.model.Location;
import com.kuaidaoresume.matching.model.Resume;
import com.kuaidaoresume.matching.model.TailoredJob;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataMongoTest
public class TailoredJobRepositoryTest {

    private static final String JOB_UUID = "job uuid";

    @Autowired
    private TailoredJobRepository tailoredJobRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    private Resume resume1;

    private Resume resume2;

    private Resume resume3;

    private TailoredJob tailoredJob;

    @BeforeEach
    public void setup() {
        resumeRepository.deleteAll();
        tailoredJobRepository.deleteAll();

        Location location = Location.builder()
            .country("Canada")
            .city("Vancouver")
            .build();

        resume1 = Resume.builder()
            .id("1")
            .userId("user id")
            .resumeUuid("resume uuid 1")
            .location(location)
            .build();
        resume2 = Resume.builder()
            .id("2")
            .userId("user id")
            .resumeUuid("resume uuid 2")
            .location(location)
            .build();
        resume3 = Resume.builder()
            .id("3")
            .userId("user id")
            .resumeUuid("resume uuid 3")
            .location(location)
            .build();

        List<Resume> resumes = Arrays.asList(resume1, resume2, resume3);
        resumeRepository.saveAll(resumes);
        tailoredJob = tailoredJobRepository.save(TailoredJob.builder()
            .jobUuid(JOB_UUID)
            .tailoredResumes(resumes)
            .build()
        );
    }

    @Test
    public void test_findByJobUuid() {
        assertThat(tailoredJobRepository.findByJobUuid(JOB_UUID).get(), is(tailoredJob));
    }

    @Test
    public void test_findByJobUuidWhenNotExist() {
        assertFalse(tailoredJobRepository.findByJobUuid("some uuid").isPresent());
    }

    @Test
    public void test_findTailoredResumesWithLimit() {
        List<Resume> tailoredResumes = tailoredJobRepository.findTailoredResumesWithLimit(JOB_UUID, 0, 1);
        assertThat(tailoredResumes.size(), is(1));
        assertThat(tailoredResumes.get(0), is(resume1));
    }

    @Test
    public void test_findTailoredResumesWithLimitAndOffset() {
        List<Resume> tailoredResumes = tailoredJobRepository.findTailoredResumesWithLimit(JOB_UUID, 1, 2);
        assertThat(tailoredResumes.size(), is(2));
        assertThat(tailoredResumes, is(Arrays.asList(resume2, resume3)));
    }

    @Test
    public void test_findTailoredResumesWithOffLimitAndOffset() {
        List<Resume> tailoredResumes = tailoredJobRepository.findTailoredResumesWithLimit(JOB_UUID, 1, 3);
        assertThat(tailoredResumes.size(), is(2));
        assertThat(tailoredResumes, is(Arrays.asList(resume2, resume3)));
    }

    @Test
    public void test_findTailoredResumesWithLimitAndExceededOffset() {
        List<Resume> tailoredResumes = tailoredJobRepository.findTailoredResumesWithLimit(JOB_UUID, 3, 3);
        assertTrue(tailoredResumes.isEmpty());
    }
}
