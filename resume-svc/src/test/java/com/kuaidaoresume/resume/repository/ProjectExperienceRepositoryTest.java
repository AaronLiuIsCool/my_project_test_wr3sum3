package com.kuaidaoresume.resume.repository;

import com.kuaidaoresume.resume.config.JpaTestConfig;
import com.kuaidaoresume.resume.model.ProjectExperience;
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
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("ut")
@TestExecutionListeners({
    DependencyInjectionTestExecutionListener.class
})
@Import(JpaTestConfig.class)
public class ProjectExperienceRepositoryTest {
    
    private static final LocalDate NOW = LocalDate.now();

    private static final String ROLE = "CEO";
    private static final String ORGANIZATION = "Delos Inc.";
    private static final String COUNTRY = "Canada";
    private static final String CITY = "Vancouver";
    private static final String DESCRIPTION = "I nailed it.";
    
    private Resume resume;
    private ProjectExperience projectExperience;

    @Autowired
    private ProjectExperienceRepository projectExperienceRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    @BeforeEach
    public void setup() {
        resume = resumeRepository.save(Resume.builder().language("en").build());

        projectExperience = ProjectExperience.builder()
            .role(ROLE)
            .organization(ORGANIZATION)
            .city(CITY)
            .country(COUNTRY)
            .startDate(NOW)
            .endDate(NOW)
            .description(DESCRIPTION)
            .resume(resume)
            .build();
        projectExperienceRepository.save(projectExperience);
    }

    @Test
    public void whenSaved_thenFindByResumeId() {
        Collection<ProjectExperience> educations = projectExperienceRepository.findAllByResumeId(resume.getId());
        assertThat(educations.size(), is(1));
    }

    @Test
    public void whenSaved_thenUpdate() {
        ProjectExperience toUpdate = projectExperience;
        String role = "CFO";
        toUpdate.setRole(role);
        ProjectExperience updated = projectExperienceRepository.save(toUpdate);
        assertThat(projectExperienceRepository.findById(updated.getId()).get().getRole(), is(role));
    }

    @Test
    public void whenSaveAll_thenAllSaved() {
        ProjectExperience experience1 = ProjectExperience.builder()
            .role(ROLE)
            .organization(ORGANIZATION)
            .city(CITY)
            .country(COUNTRY)
            .startDate(NOW)
            .endDate(NOW)
            .description(DESCRIPTION)
            .resume(resume)
            .build();

        ProjectExperience experience2 = ProjectExperience.builder()
            .role(ROLE)
            .organization(ORGANIZATION)
            .city(CITY)
            .country(COUNTRY)
            .startDate(NOW)
            .endDate(NOW)
            .description(DESCRIPTION)
            .resume(resume)
            .build();

        projectExperienceRepository.saveAll(Arrays.asList(experience1, experience2));
        assertThat(projectExperienceRepository.count(), is(3L));
    }

    @Test
    public void whenSaveAll_thenUpdateExisting() {
        ProjectExperience experience1 = ProjectExperience.builder()
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
        projectExperience.setCountry(country);
        projectExperienceRepository.saveAll(Arrays.asList(experience1, projectExperience));
        assertThat(projectExperienceRepository.findById(projectExperience.getId()).get().getCountry(), is(country));
    }

    @Test
    public void whenSaved_thenDelete() {
        Long id = projectExperience.getId();
        projectExperienceRepository.deleteById(id);
        assertFalse(projectExperienceRepository.findById(id).isPresent());
    }

    @Test
    public void whenSaved_thenDeleteAllByResumeId() {
        ProjectExperience experience1 = ProjectExperience.builder()
            .role(ROLE)
            .organization(ORGANIZATION)
            .city(CITY)
            .country(COUNTRY)
            .startDate(NOW)
            .endDate(NOW)
            .description(DESCRIPTION)
            .resume(resume)
            .build();
        projectExperienceRepository.save(experience1);
        projectExperienceRepository.deleteAll(projectExperienceRepository.findAllByResumeId(resume.getId()));
        assertThat(projectExperienceRepository.findAllByResumeId(resume.getId()).size(), is(0));
    }
}
