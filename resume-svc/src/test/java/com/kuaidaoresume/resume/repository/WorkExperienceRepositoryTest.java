package com.kuaidaoresume.resume.repository;

import com.kuaidaoresume.resume.config.JpaTestConfig;
import com.kuaidaoresume.resume.model.Resume;
import com.kuaidaoresume.resume.model.WorkExperience;
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

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@TestExecutionListeners({
    DependencyInjectionTestExecutionListener.class
})
@Import(JpaTestConfig.class)
public class WorkExperienceRepositoryTest {
    
    private static final Date NOW = Date.valueOf(LocalDate.now());

    private static final String ROLE = "CEO";
    private static final String ORGANIZATION = "Delos Inc.";
    private static final String COUNTRY = "Canada";
    private static final String CITY = "Vancouver";
    private static final String DESCRIPTION = "I nailed it.";
    
    private Resume resume;
    private WorkExperience workExperience;

    @Autowired
    private WorkExperienceRepository workExperienceRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    @BeforeEach
    public void setup() {
        resume = resumeRepository.save(new Resume());

        workExperience = WorkExperience.builder()
            .role(ROLE)
            .organization(ORGANIZATION)
            .city(CITY)
            .country(COUNTRY)
            .startDate(NOW)
            .endDate(NOW)
            .description(DESCRIPTION)
            .resume(resume)
            .build();
        workExperienceRepository.save(workExperience);
    }

    @Test
    public void whenSaved_thenFindByResumeId() {
        Collection<WorkExperience> educations = workExperienceRepository.findAllByResumeId(resume.getId());
        assertThat(educations.size(), is(1));
    }

    @Test
    public void whenSaved_thenUpdate() {
        WorkExperience toUpdate = workExperience;
        String role = "CFO";
        toUpdate.setRole(role);
        WorkExperience updated = workExperienceRepository.save(toUpdate);
        assertThat(workExperienceRepository.findById(updated.getId()).get().getRole(), is(role));
    }

    @Test
    public void whenSaveAll_thenAllSaved() {
        WorkExperience experience1 = WorkExperience.builder()
            .role(ROLE)
            .organization(ORGANIZATION)
            .city(CITY)
            .country(COUNTRY)
            .startDate(NOW)
            .endDate(NOW)
            .description(DESCRIPTION)
            .resume(resume)
            .build();

        WorkExperience experience2 = WorkExperience.builder()
            .role(ROLE)
            .organization(ORGANIZATION)
            .city(CITY)
            .country(COUNTRY)
            .startDate(NOW)
            .endDate(NOW)
            .description(DESCRIPTION)
            .resume(resume)
            .build();

        workExperienceRepository.saveAll(Arrays.asList(experience1, experience2));
        assertThat(workExperienceRepository.count(), is(3L));
    }

    @Test
    public void whenSaveAll_thenUpdateExisting() {
        WorkExperience experience1 = WorkExperience.builder()
            .role(ROLE)
            .organization(ORGANIZATION)
            .city(CITY)
            .country(COUNTRY)
            .startDate(NOW)
            .endDate(NOW)
            .description(DESCRIPTION)
            .resume(resume)
            .build();

        String country = "China";
        workExperience.setCountry(country);
        workExperienceRepository.saveAll(Arrays.asList(experience1, workExperience));
        assertThat(workExperienceRepository.findById(workExperience.getId()).get().getCountry(), is(country));
    }

    @Test
    public void whenSaved_thenDelete() {
        Long id = workExperience.getId();
        workExperienceRepository.deleteById(id);
        assertFalse(workExperienceRepository.findById(id).isPresent());
    }

    @Test
    public void whenSaved_thenDeleteAllByResumeId() {
        WorkExperience experience1 = WorkExperience.builder()
            .role(ROLE)
            .organization(ORGANIZATION)
            .city(CITY)
            .country(COUNTRY)
            .startDate(NOW)
            .endDate(NOW)
            .description(DESCRIPTION)
            .resume(resume)
            .build();
        workExperienceRepository.save(experience1);
        workExperienceRepository.deleteAll(workExperienceRepository.findAllByResumeId(resume.getId()));
        assertThat(workExperienceRepository.findAllByResumeId(resume.getId()).size(), is(0));
    }
}
