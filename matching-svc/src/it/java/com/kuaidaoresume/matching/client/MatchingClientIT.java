package com.kuaidaoresume.matching.client;

import com.kuaidaoresume.common.api.BaseResponse;
import com.kuaidaoresume.common.auth.AuthConstant;
import com.kuaidaoresume.matching.dto.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("it")
@ExtendWith(SpringExtension.class)
@EnableFeignClients(basePackages = {"com.kuaidaoresume.matching.client"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@EnableAutoConfiguration
public class MatchingClientIT {

    private static final String MAJOR = "Anthroposomatology";
    private static final String KEYWORD = "NCCA-Accredited";
    private static final String COUNTRY = "United States";
    private static final String CITY = "New York";
    private static final String JOB_TITLE = "Personal Trainer";
    private static final String COMPANY_NAME = "Tonal";
    private static final String JOB_UUID = "4b923a17-649c-4135-91c3-e5cfefa9c0bf";
    private static final String RESUME_UUID = "2d034692-16bb-4c57-95dd-29be2554254e";
    private static final String USER_UUID = "334a77f1-5b69-4d6e-80ba-bf5da5d00b54";
    private static final String JOB_URL = "google.com";

    @Autowired
    private MatchingClient matchingClient;

    private JobDto job;
    private ResumeDto resume;
    private LocationDto location;

    @BeforeAll
    public void setup() {
        CompensationDto compensation = CompensationDto.builder()
            .currency("us")
            .type("wage")
            .highBound("2")
            .lowBound("1")
            .build();

        KeywordDto keyword = KeywordDto.builder().value(KEYWORD).rating(1.0).build();

        location = LocationDto.builder()
            .country(COUNTRY)
            .city(CITY)
            .build();

        job = JobDto.builder()
            .jobUuid(JOB_UUID)
            .location(location)
            .companyName(COMPANY_NAME)
            .title(JOB_TITLE)
            .compensation(compensation)
            .employmentType("Contract")
            .jobType("Part-time")
            .keywords(Arrays.asList(keyword))
            .relevantMajors(Arrays.asList(MAJOR))
            .postDate(LocalDateTime.of(2020, 1, 1, 0, 0))
            .url(JOB_URL)
            .build();

        resume = ResumeDto.builder()
            .userId(USER_UUID)
            .resumeUuid(RESUME_UUID)
            .location(location)
            .resumeName("my resume")
            .keywords(Arrays.asList(KEYWORD))
            .majors(Arrays.asList(MAJOR))
            .build();
    }

    @Test
    @Order(1)
    public void test_addJob() {
        BaseResponse response = matchingClient.addJob(AuthConstant.AUTHORIZATION_JOB_SERVICE, job);
        assertTrue(response.isSuccess());
    }

    @Test
    @Order(2)
    public void test_addResume() {
        BaseResponse response = matchingClient.addResume(AuthConstant.AUTHORIZATION_RESUME_SERVICE, resume);
        assertTrue(response.isSuccess());
    }

    @Test
    @Order(3)
    public void test_matching() {
        JobListResponse jobListResponse = matchingClient.findMatchedJobs(AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, resume);
        assertTrue(jobListResponse.isSuccess());
        assertThat(jobListResponse.getJobList().getJobs(), is(Arrays.asList(this.job)));

        ResumeListResponse resumeListResponse = matchingClient.findMatchedResumes(AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, this.job);
        assertTrue(resumeListResponse.isSuccess());
        assertThat(resumeListResponse.getResumeList().getResumes(), is(Arrays.asList(resume)));
    }

    @Test
    @Order(4)
    public void test_tailoring() throws InterruptedException {
        BaseResponse response = matchingClient.addTailoredResume(
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, RESUME_UUID, JOB_UUID, true);
        assertTrue(response.isSuccess());

        GenericJobResponse jobResponse = matchingClient.getTailoredJob(
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, RESUME_UUID);
        assertTrue(jobResponse.isSuccess());
        assertThat(jobResponse.getJobDto(), is(job));

        ResumeListResponse resumeListResponse = matchingClient.getTailoredResumes(
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, JOB_UUID);
        assertTrue(resumeListResponse.isSuccess());
        assertThat(resumeListResponse.getResumeList().getResumes(), is(Arrays.asList(resume)));

        // job is bookmarked async
        int retries = 0;
        int maxRetries = 3;
        JobListResponse jobListResponse;
        do {
            Thread.sleep(100 * retries);
            jobListResponse = matchingClient.getBookmarkedJobs(
                AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, RESUME_UUID);
            assertTrue(jobListResponse.isSuccess());
        } while (jobListResponse.getJobList().getJobs().isEmpty() && ++retries < maxRetries);
        assertThat(jobListResponse.getJobList().getJobs(), is(Arrays.asList(job)));
    }

    @Test
    @Order(5)
    public void test_bookmarking() {
        String anotherJobUuid = "5ef2bc3e-9902-4d0a-b06e-e5b06eb5f6a2";
        JobDto anotherJob = JobDto.builder()
            .jobUuid(anotherJobUuid)
            .location(location)
            .companyName(COMPANY_NAME)
            .title(JOB_TITLE)
            .postDate(LocalDateTime.of(2020, 1, 1, 0, 0))
            .build();
        matchingClient.addJob(AuthConstant.AUTHORIZATION_JOB_SERVICE, anotherJob);

        BaseResponse response = matchingClient.bookmarkJob(
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, RESUME_UUID, anotherJobUuid);
        assertTrue(response.isSuccess());

        JobListResponse jobListResponse = matchingClient.getBookmarkedJobs(
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, RESUME_UUID);
        assertTrue(jobListResponse.isSuccess());
        assertThat(jobListResponse.getJobList().getJobs(), is(Arrays.asList(job, anotherJob)));

        jobListResponse = matchingClient.getBookmarkedJobs(
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, RESUME_UUID, 1, 1);
        assertTrue(jobListResponse.isSuccess());
        assertThat(jobListResponse.getJobList().getJobs(), is(Arrays.asList(anotherJob)));

        ResumeListResponse resumeListResponse = matchingClient.getBookmarkedResumes(
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, JOB_UUID);
        assertTrue(resumeListResponse.isSuccess());
        assertThat(resumeListResponse.getResumeList().getResumes(), is(Arrays.asList(resume)));
    }

    @Test
    @Order(6)
    public void test_visiting() {
        BaseResponse response = matchingClient.visitJob(
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, RESUME_UUID, JOB_UUID);
        assertTrue(response.isSuccess());

        JobListResponse jobListResponse = matchingClient.getVisitedJobs(
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, RESUME_UUID);
        assertTrue(jobListResponse.isSuccess());
        assertThat(jobListResponse.getJobList().getJobs(), is(Arrays.asList(job)));

        ResumeListResponse resumeListResponse = matchingClient.getVisitedResumes(
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, JOB_UUID);
        assertTrue(resumeListResponse.isSuccess());
        assertThat(resumeListResponse.getResumeList().getResumes(), is(Arrays.asList(resume)));
    }
}
