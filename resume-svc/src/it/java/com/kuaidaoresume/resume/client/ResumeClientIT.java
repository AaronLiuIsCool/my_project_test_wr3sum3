package com.kuaidaoresume.resume.client;

import com.kuaidaoresume.common.auth.AuthConstant;
import com.kuaidaoresume.resume.dto.*;
import com.kuaidaoresume.resume.model.Profile;
import com.kuaidaoresume.resume.service.rating.Rating;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("it")
@ExtendWith(SpringExtension.class)
@EnableFeignClients(basePackages = {"com.kuaidaoresume.resume.client"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@EnableAutoConfiguration
public class ResumeClientIT {

    private static final String FULL_NAME = "fullName";
    private static final String ALIAS = "alias";
    private static final String COUNTRY = "Canada";
    private static final String PROVINCE = "province";
    private static final String CITY = "city";
    private static final String EMAIL = "test@email.com";
    private static final String PHONE_NUMBER = "alias";
    private static final String PROFILE_URL= "http://linkedin.com/dongwang";
    private static final Profile.ProfileType PROFILE_TYPE = Profile.ProfileType.LINKEDIN;
    private static final String LANGUAGE = "en";
    private static final String INSTITUTION = "UBC";
    private static final String MAJOR = "arts";
    private static final String DEGREE = "master";
    private static final String GPA = "4.0";
    private static final String START_DATE = "2018-01-02";
    private static final String END_DATE = "2020-01-02";
    private static final String AWARD_NAME = "nobel";
    private static final String ROLE = "CEO";
    private static final String ORGANIZATION = "Delos Inc.";
    private static final String DESCRIPTION = "1. blah big data blah 100\n2. blah blah google blah 100%\n3.blah blah tensor";
    private static final String NAME = "CPA";
    private static final String ISSUE_DATE = "2020-01-02";
    private static final String EXPIRATION_DATE = "2020-01-03";

    private ProfileDto profile;
    private BasicInfoDto basicInfo;
    private ResumeDto resume;
    private EducationDto education;
    private AwardDto award;
    private ExperienceDto workExperience;
    private CertificateDto certificate;

    private String resumeId;

    @Autowired
    private ResumeClient resumeClient;

    @BeforeAll
    public void setup() {
        profile = ProfileDto.builder()
            .type(PROFILE_TYPE)
            .url(PROFILE_URL)
            .build();

        basicInfo = BasicInfoDto.builder()
            .fullName(FULL_NAME)
            .alias(ALIAS)
            .country(COUNTRY)
            .province(PROVINCE)
            .city(CITY)
            .email(EMAIL)
            .phoneNumber(PHONE_NUMBER)
            .profiles(Arrays.asList(profile))
            .build();

        education = EducationDto.builder()
            .country(COUNTRY)
            .city(CITY)
            .institution(INSTITUTION)
            .major(MAJOR)
            .degree(DEGREE)
            .gpa(GPA)
            .startDate(START_DATE)
            .endDate(END_DATE)
            .build();
        award = new AwardDto(AWARD_NAME);
        education.setAwards(Arrays.asList(award));

        workExperience = ExperienceDto.builder()
            .role(ROLE)
            .organization(ORGANIZATION)
            .city(CITY)
            .country(COUNTRY)
            .startDate(START_DATE)
            .endDate(END_DATE)
            .description(DESCRIPTION)
            .build();

        certificate = CertificateDto.builder()
            .name(NAME)
            .issueDate(ISSUE_DATE)
            .expirationDate(EXPIRATION_DATE)
            .build();

        resume = ResumeDto.builder()
            .language(LANGUAGE)
            .basicInfo(basicInfo)
            .educations(Arrays.asList(education))
            .workExperiences(Arrays.asList(workExperience))
            .certificates(Arrays.asList(certificate))
            .build();
    }

    @Test
    @Order(1)
    public void test_createAndGetResume() {
        // Create
        ResponseEntity<EntityModel<PersistedResumeDto>> responseEntity =
            resumeClient.createResume(AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, resume);
        assertThat(responseEntity.getStatusCode(), is(CREATED));
        resumeId = responseEntity.getBody().getContent().getId();

        // Get
        responseEntity =
            resumeClient.findResumeById(AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, resumeId);
        assertThat(responseEntity.getStatusCode(), is(OK));
        PersistedResumeDto result = responseEntity.getBody().getContent();

        Assertions.assertThat(result.getBasicInfo())
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(basicInfo);

        Assertions.assertThat(result.getEducations())
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(Arrays.asList(education));

        Assertions.assertThat(result.getWorkExperiences())
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(Arrays.asList(workExperience));

        Assertions.assertThat(result.getCertificates())
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(Arrays.asList(certificate));
    }

    @Test
    @Order(2)
    public void test_getResumeRating() {
        ResponseEntity<EntityModel<ResumeRatingDto>> responseEntity =
            resumeClient.getResumeRating(AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, resumeId);
        assertThat(responseEntity.getStatusCode(), is(OK));
        ResumeRatingDto result = responseEntity.getBody().getContent();

        EducationRatingDto educationRatingDto = EducationRatingDto.builder()
            .gpa(education.getGpa())
            .major(education.getMajor())
            .institution(education.getInstitution())
            .rating(Rating.GREAT)
            .build();

        BulletDto bullet1 = BulletDto.builder()
            .bullet("blah big data blah 100")
            .numericWords(Arrays.asList("100"))
            .keywords(Arrays.asList("big data"))
            .build();

        BulletDto bullet2 = BulletDto.builder()
            .bullet("blah blah google blah 100%")
            .numericWords(Arrays.asList("100%"))
            .keywords(Collections.emptyList())
            .build();

        BulletDto bullet3 = BulletDto.builder()
            .bullet("blah blah tensor")
            .numericWords(Collections.emptyList())
            .keywords(Arrays.asList("tensor"))
            .build();

        ExperienceRatingDto experienceRatingDto = ExperienceRatingDto.builder()
            .role(workExperience.getRole())
            .organization(workExperience.getOrganization())
            .bullets(Arrays.asList(bullet1, bullet2, bullet3))
            .rating(Rating.MEDIOCRE)
            .build();

        int expectedScore = 63;
        ResumeRatingDto expected = ResumeRatingDto.builder()
            .resumeUuid(resumeId)
            .score(expectedScore)
            .educations(Arrays.asList(educationRatingDto))
            .workExperiences(Arrays.asList(experienceRatingDto))
            .projectExperiences(Collections.emptyList())
            .volunteerExperiences(Collections.emptyList())
            .numCertificates(1)
            .build();

        Assertions.assertThat(result)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(expected);
    }

    @Test
    @Order(3)
    public void test_basicInfoCRU() {
        // Create an empty resume
        ResumeDto emptyResume = ResumeDto.builder()
            .language(LANGUAGE)
            .build();
        ResponseEntity<EntityModel<PersistedResumeDto>> resumeResponse =
            resumeClient.createResume(AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, emptyResume);
        assertThat(resumeResponse.getStatusCode(), is(CREATED));
        resumeId = resumeResponse.getBody().getContent().getId();

        // Create basic info
        ResponseEntity<EntityModel<PersistedBasicInfoDto>> basicInfoResponse =
            resumeClient.createBasicInfo(AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, resumeId, basicInfo);
        assertThat(basicInfoResponse.getStatusCode(), is(CREATED));

        // Get basic info
        basicInfoResponse = resumeClient.findBasicInfoByResumeId(AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, resumeId);
        assertThat(basicInfoResponse.getStatusCode(), is(OK));
        PersistedBasicInfoDto result = basicInfoResponse.getBody().getContent();
        Assertions.assertThat(result)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(basicInfo);

        // update basic info
        String newEmail = "newEmail@test.com";
        result.setEmail(newEmail);
        resumeClient.saveBasicInfo(AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, resumeId, result);
        PersistedBasicInfoDto updated =
            resumeClient.findBasicInfoByResumeId(AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, resumeId).getBody().getContent();
        assertThat(updated.getEmail(), is(newEmail));
    }

    @Test
    @Order(4)
    public void test_educationCRU() {
        // Create education
        ResponseEntity<EntityModel<PersistedEducationDto>> responseEntity =
            resumeClient.createEducation(AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, resumeId, education);
        assertThat(responseEntity.getStatusCode(), is(CREATED));
        Long id = responseEntity.getBody().getContent().getId();

        // Get education
        responseEntity = resumeClient.findEducationById(AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, id);
        assertThat(responseEntity.getStatusCode(), is(OK));
        PersistedEducationDto result = responseEntity.getBody().getContent();
        Assertions.assertThat(result)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(education);

        // update education
        String newGpa = "100";
        result.setGpa(newGpa);
        resumeClient.saveEducation(AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, id, result);
        PersistedEducationDto updated =
            resumeClient.findEducationById(AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, id).getBody().getContent();
        assertThat(updated.getGpa(), is(newGpa));
    }

    @Test
    @Order(5)
    public void test_experienceCRU() {
        // Create experience
        ResponseEntity<EntityModel<PersistedWorkExperienceDto>> responseEntity =
            resumeClient.createWorkExperience(AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, resumeId, workExperience);
        assertThat(responseEntity.getStatusCode(), is(CREATED));
        Long id = responseEntity.getBody().getContent().getId();

        // Get experience
        responseEntity = resumeClient.findWorkExperienceById(AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, id);
        assertThat(responseEntity.getStatusCode(), is(OK));
        PersistedWorkExperienceDto result = responseEntity.getBody().getContent();
        Assertions.assertThat(result)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(workExperience);

        // update experience
        String newRole = "CFO";
        result.setRole(newRole);
        resumeClient.saveWorkExperience(AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, id, result);
        PersistedWorkExperienceDto updated =
            resumeClient.findWorkExperienceById(AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, id).getBody().getContent();
        assertThat(updated.getRole(), is(newRole));
    }

    @Test
    @Order(6)
    public void test_certificateCRU() {
        // Create experience
        ResponseEntity<EntityModel<PersistedCertificateDto>> responseEntity =
            resumeClient.createCertificate(AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, resumeId, certificate);
        assertThat(responseEntity.getStatusCode(), is(CREATED));
        Long id = responseEntity.getBody().getContent().getId();

        // Get experience
        responseEntity = resumeClient.findCertificateById(AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, id);
        assertThat(responseEntity.getStatusCode(), is(OK));
        PersistedCertificateDto result = responseEntity.getBody().getContent();
        Assertions.assertThat(result)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(certificate);

        // update experience
        String newExpirationDate = "2050-01-01";
        result.setExpirationDate(newExpirationDate);
        resumeClient.saveCertificate(AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, id, result);
        PersistedCertificateDto updated =
            resumeClient.findCertificateById(AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, id).getBody().getContent();
        assertThat(updated.getExpirationDate(), is(newExpirationDate));
    }
}


