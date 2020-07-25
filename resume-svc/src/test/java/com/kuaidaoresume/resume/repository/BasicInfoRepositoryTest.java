package com.kuaidaoresume.resume.repository;

import com.kuaidaoresume.resume.config.JpaTestConfig;
import com.kuaidaoresume.resume.model.BasicInfo;
import com.kuaidaoresume.resume.model.Profile;
import com.kuaidaoresume.resume.model.Resume;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@TestExecutionListeners({
    DependencyInjectionTestExecutionListener.class
})
@Import(JpaTestConfig.class)
public class BasicInfoRepositoryTest {

    private Resume resume;
    private BasicInfo basicInfo;
    private Profile profile;
    private String resumeId;

    @Autowired
    private BasicInfoRepository basicInfoRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private TestProfileRepository testProfileRepository;

    @BeforeEach
    public void setup() {
        resume = resumeRepository.save(Resume.builder().language("en").build());
        resumeId = resume.getId();
        basicInfo = BasicInfo.builder()
            .fullName("Donald Trump")
            .alias("Jianguo")
            .country("Russia")
            .city("Beijing")
            .email("spy@whitehouse.com")
            .phoneNumber("123456789")
            .resume(resume)
            .build();
        profile = Profile.builder()
            .type(Profile.ProfileType.LINKEDIN)
            .url("https://www.linkedin.com/mcga")
            .basicInfo(basicInfo)
            .build();
        basicInfo.setProfiles(Arrays.asList(profile));
        basicInfoRepository.save(basicInfo);
    }

    @Test
    public void whenSaved_thenFindByResumeId() {
        BasicInfo savedBasicInfo = basicInfoRepository.findByResumeId(resumeId).get();
        assertNotNull(savedBasicInfo);
    }

    @Test
    public void whenSaved_thenFindProfile() {
        assertTrue(testProfileRepository.findAll().iterator().hasNext());
    }

    @Test
    public void whenSaved_thenUpdate() {
        String newPhoneNo = "987654321";
        basicInfo.setPhoneNumber(newPhoneNo);
        BasicInfo updated = basicInfoRepository.save(basicInfo);
        assertThat(updated.getPhoneNumber(), is(newPhoneNo));
    }
}
