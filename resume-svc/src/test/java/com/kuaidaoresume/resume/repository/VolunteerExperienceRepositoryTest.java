package com.kuaidaoresume.resume.repository;

import com.kuaidaoresume.resume.config.JpaTestConfig;
import com.kuaidaoresume.resume.model.Resume;
import com.kuaidaoresume.resume.model.VolunteerExperience;
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
public class VolunteerExperienceRepositoryTest {
    
    private static final Date NOW = Date.valueOf(LocalDate.now());

    private static final String ROLE = "CEO";
    private static final String ORGANIZATION = "Delos Inc.";
    private static final String COUNTRY = "Canada";
    private static final String CITY = "Vancouver";
    private static final String DESCRIPTION = "I nailed it.";
    
    private Resume resume;
    private VolunteerExperience volunteerExperience;

    @Autowired
    private VolunteerExperienceRepository volunteerExperienceRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    @BeforeEach
    public void setup() {
        resume = resumeRepository.save(Resume.builder().language("en").build());

        volunteerExperience = VolunteerExperience.builder()
            .role(ROLE)
            .organization(ORGANIZATION)
            .city(CITY)
            .country(COUNTRY)
            .startDate(NOW)
            .endDate(NOW)
            .description(DESCRIPTION)
            .resume(resume)
            .build();
        volunteerExperienceRepository.save(volunteerExperience);
    }

    @Test
    public void whenSaved_thenFindByResumeId() {
        Collection<VolunteerExperience> educations = volunteerExperienceRepository.findAllByResumeId(resume.getId());
        assertThat(educations.size(), is(1));
    }

    @Test
    public void whenSaved_thenUpdate() {
        VolunteerExperience toUpdate = volunteerExperience;
        String role = "CFO";
        toUpdate.setRole(role);
        VolunteerExperience updated = volunteerExperienceRepository.save(toUpdate);
        assertThat(volunteerExperienceRepository.findById(updated.getId()).get().getRole(), is(role));
    }

    @Test
    public void whenSaveAll_thenAllSaved() {
        VolunteerExperience experience1 = VolunteerExperience.builder()
            .role(ROLE)
            .organization(ORGANIZATION)
            .city(CITY)
            .country(COUNTRY)
            .startDate(NOW)
            .endDate(NOW)
            .description(DESCRIPTION)
            .resume(resume)
            .build();

        VolunteerExperience experience2 = VolunteerExperience.builder()
            .role(ROLE)
            .organization(ORGANIZATION)
            .city(CITY)
            .country(COUNTRY)
            .startDate(NOW)
            .endDate(NOW)
            .description(DESCRIPTION)
            .resume(resume)
            .build();

        volunteerExperienceRepository.saveAll(Arrays.asList(experience1, experience2));
        assertThat(volunteerExperienceRepository.count(), is(3L));
    }

    @Test
    public void whenSaveAll_thenUpdateExisting() {
        VolunteerExperience experience1 = VolunteerExperience.builder()
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
        volunteerExperience.setCountry(country);
        volunteerExperienceRepository.saveAll(Arrays.asList(experience1, volunteerExperience));
        assertThat(volunteerExperienceRepository.findById(volunteerExperience.getId()).get().getCountry(), is(country));
    }

    @Test
    public void whenSaved_thenDelete() {
        Long id = volunteerExperience.getId();
        volunteerExperienceRepository.deleteById(id);
        assertFalse(volunteerExperienceRepository.findById(id).isPresent());
    }

    @Test
    public void whenSaved_thenDeleteAllByResumeId() {
        VolunteerExperience experience1 = VolunteerExperience.builder()
            .role(ROLE)
            .organization(ORGANIZATION)
            .city(CITY)
            .country(COUNTRY)
            .startDate(NOW)
            .endDate(NOW)
            .description(DESCRIPTION)
            .resume(resume)
            .build();
        volunteerExperienceRepository.save(experience1);
        volunteerExperienceRepository.deleteAll(volunteerExperienceRepository.findAllByResumeId(resume.getId()));
        assertThat(volunteerExperienceRepository.findAllByResumeId(resume.getId()).size(), is(0));
    }
}
