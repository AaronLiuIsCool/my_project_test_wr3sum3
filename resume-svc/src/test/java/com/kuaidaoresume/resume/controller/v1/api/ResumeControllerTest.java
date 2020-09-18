package com.kuaidaoresume.resume.controller.v1.api;

import com.kuaidaoresume.resume.config.ResumeApplicationTestConfig;
import com.kuaidaoresume.resume.controller.v1.assembler.ResumeMatchingRepresentationModelAssembler;
import com.kuaidaoresume.resume.controller.v1.assembler.ResumeRatingRepresentationModelAssembler;
import com.kuaidaoresume.resume.controller.v1.assembler.ResumeRepresentationModelAssembler;
import com.kuaidaoresume.resume.controller.v1.assembler.ResumeScoreRepresentationModelAssembler;
import com.kuaidaoresume.resume.model.*;
import com.kuaidaoresume.resume.service.ResumeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;
import java.util.TimeZone;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ResumeController.class)
@Import({
    ResumeApplicationTestConfig.class,
    ResumeRepresentationModelAssembler.class,
    ResumeScoreRepresentationModelAssembler.class,
    ResumeMatchingRepresentationModelAssembler.class,
    ResumeRatingRepresentationModelAssembler.class
})
public class ResumeControllerTest {

    private static final String RESUME_ID = "aUUID";
    private static final String FULL_NAME = "fullName";
    private static final Long BASIC_INFO_ID = 1L;
    private static final String ALIAS = "alias";
    private static final String COUNTRY = "Canada";
    private static final String PROVINCE = "province";
    private static final String CITY = "city";
    private static final String EMAIL = "test@email.com";
    private static final String PHONE_NUMBER = "alias";
    private static final String PROFILE_URL= "profileUrl";
    private static final Profile.ProfileType PROFILE_TYPE = Profile.ProfileType.LINKEDIN;
    private static final String LANGUAGE = "en";
    private static final Long EDUCATION_ID = 1L;
    private static final String INSTITUTION = "UBC";
    private static final String MAJOR = "arts";
    private static final String DEGREE = "master";
    private static final String TIMEZONE = TimeZone.getDefault().getDisplayName();
    private static final String GPA = "4.0";
    private static final Date START_DATE = Date.valueOf(LocalDate.of(2000, 1, 1));
    private static final Date END_DATE = Date.valueOf(LocalDate.of(2001, 1, 1));
    private static final String AWARD_NAME = "nobel";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final String START_DATE_TEXT = START_DATE.toLocalDate().format(FORMATTER);
    private static final String END_DATE_TEXT = END_DATE.toLocalDate().format(FORMATTER);
    private static final Long WORK_EXPERIENCE_ID = 1L;
    private static final String ROLE = "CEO";
    private static final String ORGANIZATION = "Delos Inc.";
    private static final String DESCRIPTION = "I nailed it.";
    private static final Long CERTIFICATE_ID = 1L;
    private static final String NAME = "CPA";
    private static final Date ISSUE_DATE = Date.valueOf(LocalDate.of(2000, 1, 1));
    private static final Date EXPIRATION_DATE = Date.valueOf(LocalDate.of(2001, 1, 1));
    private static final String ISSUE_DATE_TEXT = ISSUE_DATE.toLocalDate().format(FORMATTER);
    private static final String EXPIRATION_DATE_TEXT = EXPIRATION_DATE.toLocalDate().format(FORMATTER);

    private Profile profile;
    private BasicInfo basicInfo;
    private Resume resume;
    private Education education;
    private Award award;
    private WorkExperience workExperience;
    private Certificate certificate;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ResumeService resumeService;

    @BeforeEach
    public void setup() {
        profile = Profile.builder()
            .type(PROFILE_TYPE)
            .url(PROFILE_URL)
            .build();

        basicInfo = BasicInfo.builder()
            .id(BASIC_INFO_ID)
            .fullName(FULL_NAME)
            .alias(ALIAS)
            .country(COUNTRY)
            .province(PROVINCE)
            .city(CITY)
            .email(EMAIL)
            .phoneNumber(PHONE_NUMBER)
            .profiles(Arrays.asList(profile))
            .resume(Resume.builder().id(RESUME_ID).build())
            .build();

        education = Education.builder()
            .id(EDUCATION_ID)
            .country(COUNTRY)
            .city(CITY)
            .institution(INSTITUTION)
            .major(MAJOR)
            .degree(DEGREE)
            .gpa(GPA)
            .startDate(START_DATE)
            .endDate(END_DATE)
            .build();
        award = Award.builder().name(AWARD_NAME).education(education).build();
        education.setAwards(Arrays.asList(award));

        workExperience = WorkExperience.builder()
            .id(WORK_EXPERIENCE_ID)
            .role(ROLE)
            .organization(ORGANIZATION)
            .city(CITY)
            .country(COUNTRY)
            .startDate(START_DATE)
            .endDate(END_DATE)
            .description(DESCRIPTION)
            .build();

        certificate = Certificate.builder()
            .id(CERTIFICATE_ID)
            .name(NAME)
            .issueDate(ISSUE_DATE)
            .expirationDate(EXPIRATION_DATE)
            .build();

        resume = Resume.builder()
            .id(RESUME_ID)
            .language(LANGUAGE)
            .basicInfo(basicInfo)
            .educations(Arrays.asList(education))
            .workExperiences(Arrays.asList(workExperience))
            .certificates(Arrays.asList(certificate))
            .build();
    }

    @Test
    public void whenFindById_thenReturn200() throws Exception {
        given(resumeService.findResumeById(RESUME_ID)).willReturn(Optional.of(resume));

        mvc.perform(get("/v1/resumes/{id}", RESUME_ID).accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()))
            .andExpect(jsonPath("$.id", is(RESUME_ID)))
            .andExpect(jsonPath("$.language", is(LANGUAGE)))
            .andExpect(jsonPath("$.basicInfo.fullName", is(FULL_NAME)))
            .andExpect(jsonPath("$.basicInfo.alias", is(ALIAS)))
            .andExpect(jsonPath("$.basicInfo.country", is(COUNTRY)))
            .andExpect(jsonPath("$.basicInfo.province", is(PROVINCE)))
            .andExpect(jsonPath("$.basicInfo.city", is(CITY)))
            .andExpect(jsonPath("$.basicInfo.email", is(EMAIL)))
            .andExpect(jsonPath("$.basicInfo.phoneNumber", is(PHONE_NUMBER)))
            .andExpect(jsonPath("$.basicInfo.profiles[0].type", is(PROFILE_TYPE.name())))
            .andExpect(jsonPath("$.basicInfo.profiles[0].url", is(PROFILE_URL)))
            .andExpect(jsonPath("$.educations[0].country", is(COUNTRY)))
            .andExpect(jsonPath("$.educations[0].city", is(CITY)))
            .andExpect(jsonPath("$.educations[0].institution", is(INSTITUTION)))
            .andExpect(jsonPath("$.educations[0].major", is(MAJOR)))
            .andExpect(jsonPath("$.educations[0].degree", is(DEGREE)))
            .andExpect(jsonPath("$.educations[0].gpa", is(GPA)))
            .andExpect(jsonPath("$.educations[0].startDate", is(START_DATE_TEXT)))
            .andExpect(jsonPath("$.educations[0].endDate", is(END_DATE_TEXT)))
            .andExpect(jsonPath("$.workExperiences[0].role", is(ROLE)))
            .andExpect(jsonPath("$.workExperiences[0].organization", is(ORGANIZATION)))
            .andExpect(jsonPath("$.workExperiences[0].city", is(CITY)))
            .andExpect(jsonPath("$.workExperiences[0].country", is(COUNTRY)))
            .andExpect(jsonPath("$.workExperiences[0].startDate", is(START_DATE_TEXT)))
            .andExpect(jsonPath("$.workExperiences[0].endDate", is(END_DATE_TEXT)))
            .andExpect(jsonPath("$.workExperiences[0].description", is(DESCRIPTION)))
            .andExpect(jsonPath("$.certificates[0].name", is(NAME)))
            .andExpect(jsonPath("$.certificates[0].issueDate", is(ISSUE_DATE_TEXT)))
            .andExpect(jsonPath("$.certificates[0].expirationDate", is(EXPIRATION_DATE_TEXT)))
            .andExpect(jsonPath("$.links[0].rel", is("self")))
            .andExpect(jsonPath("$.links[0].href", is(String.format("http://localhost/v1/resumes/%s", RESUME_ID))))
            .andReturn();
    }
}


