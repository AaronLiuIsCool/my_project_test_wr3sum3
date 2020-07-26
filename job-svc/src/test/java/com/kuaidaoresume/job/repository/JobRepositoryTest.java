package com.kuaidaoresume.job.repository;

import com.kuaidaoresume.job.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@TestExecutionListeners({
    DependencyInjectionTestExecutionListener.class
})
public class JobRepositoryTest {

    private static final Long id = 1L;
    private static final Date postDate = new Date(System.currentTimeMillis());
    private static final String positionTitle = "SDE";
    private static final String companyName = "ABC";
    private static final String url = "someUrl";
    private static final String country = "aCountry";
    private static final String city = "aCity";
    private static final String postCode = "aPostCode";
    private static final String majorName = "CS";
    private static Job job;
    private static List<Major> majors;
    private static Location location;
    private static Major major;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private MajorRepository majorRepository;

    @BeforeEach
    public void setup() {
        location = Location.builder()
                                    .id(id)
                                    .country(country)
                                    .city(city)
                                    .postCode(postCode)
                                    .build();

        major = Major.builder()
                            .id(id)
                            .name(majorName)
                            .build();

        majors = Arrays.asList(major);

        job = Job.builder()
                .id(id)
                .postDate(postDate)
                .positionTitle(positionTitle)
                .companyName(companyName)
                .url(url)
                .location(location)
                .majors(majors)
                .build();
        jobRepository.save(job);
    }

    @Test
    public void whenSaved_thenFindByJobId() {
        Optional<Job> savedJob = jobRepository.findById(id);
        assertTrue(savedJob.isPresent());
    }

    @Test
    public void whenSaved_thenFindByLocationIn() {
        assertThat(jobRepository.findByLocationIn(Arrays.asList(location)).size(), is(1));
        assertThat(jobRepository.findByLocationIn(Arrays.asList(location)).get(0).getId(), is(job.getId()));
    }

    @Test
    public void whenSaved_thenFindByMajorsIn() {
        assertThat(jobRepository.findByMajorsIn(majors).size(), is(1));
        assertThat(jobRepository.findByMajorsIn(majors).get(0).getId(), is(job.getId()));
    }

    @Test
    public void whenSaved_thenSaveAgain() {
        Job savedAgain = jobRepository.save(job);
        assertThat(jobRepository.count(), is(1L));
        assertThat(savedAgain.getId(), is(job.getId()));
    }
}
