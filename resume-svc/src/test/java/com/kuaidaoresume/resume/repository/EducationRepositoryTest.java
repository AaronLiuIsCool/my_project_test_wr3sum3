package com.kuaidaoresume.resume.repository;

import com.kuaidaoresume.resume.config.JpaTestConfig;
import com.kuaidaoresume.resume.model.Award;
import com.kuaidaoresume.resume.model.Education;
import com.kuaidaoresume.resume.model.Resume;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@TestExecutionListeners({
    DependencyInjectionTestExecutionListener.class
})
@Import(JpaTestConfig.class)
public class EducationRepositoryTest {

    private static final Date NOW = Date.valueOf(LocalDate.now());
    private Resume resume;
    private Education education;
    private Award award;

    @Autowired
    private EducationRepository educationRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private TestAwardRepository awardRepository;

    @BeforeEach
    public void setup() {
        resume = resumeRepository.save(Resume.builder().language("en").build());
        education = Education.builder()
            .country("Canada")
            .city("Vancouver")
            .institution("UBC")
            .major("Math")
            .degree("PHD")
            .startDate(NOW)
            .endDate(NOW)
            .resume(resume)
            .build();
        award = Award.builder().name("nobel").education(education).build();
        education.setAwards(Arrays.asList(award));
        educationRepository.save(education);
    }

    @Test
    public void whenSaved_thenFindByResumeId() {
        Collection<Education> educations = educationRepository.findAllByResumeId(resume.getId());
        assertThat(educations.size(), is(1));
        assertTrue(awardRepository.findAll().iterator().hasNext());
    }

    @Test
    public void whenSaved_thenUpdate() {
        Education toUpdate = education;
        String gpa = "4.0";
        toUpdate.setGpa(gpa);
        Education updated = educationRepository.save(toUpdate);
        assertThat(educationRepository.findById(updated.getId()).get().getGpa(), is(gpa));
    }

    @Test
    public void whenSaveAll_thenAllSaved() {
        Education education1 = Education.builder()
            .country("Canada")
            .city("Vancouver")
            .institution("UBC")
            .major("ComSci")
            .degree("PHD")
            .startDate(NOW)
            .endDate(NOW)
            .resume(resume)
            .build();

        Education education2 = Education.builder()
            .country("Canada")
            .city("Vancouver")
            .institution("UBC")
            .major("philosophy")
            .degree("PHD")
            .startDate(NOW)
            .endDate(NOW)
            .resume(resume)
            .build();

        educationRepository.saveAll(Arrays.asList(education1, education2));
        assertThat(educationRepository.count(), is(3L));
    }

    @Test
    public void whenSaveAll_thenUpdateExisting() {
        Education education1 = Education.builder()
            .country("Canada")
            .city("Vancouver")
            .institution("UBC")
            .major("ComSci")
            .degree("PHD")
            .startDate(NOW)
            .endDate(NOW)
            .resume(resume)
            .build();

        String country = "Canada";
        education.setCountry(country);
        educationRepository.saveAll(Arrays.asList(education1, education));
        assertThat(educationRepository.findById(education.getId()).get().getCountry(), is(country));
    }

    @Test
    public void whenSaved_thenDelete() {
        Long id = education.getId();
        educationRepository.deleteById(id);
        assertFalse(educationRepository.findById(id).isPresent());
        assertNull(awardRepository.findByEducationId(id));
    }

    @Test
    public void whenSaved_thenDeleteAllByResumeId() {
        Education education1 = Education.builder()
            .country("Canada")
            .city("Vancouver")
            .institution("UBC")
            .major("Computer Science")
            .degree("PHD")
            .startDate(NOW)
            .endDate(NOW)
            .resume(resume)
            .build();
        educationRepository.save(education1);
        educationRepository.deleteAll(educationRepository.findAllByResumeId(resume.getId()));
        assertThat(educationRepository.findAllByResumeId(resume.getId()).size(), is(0));
    }
}