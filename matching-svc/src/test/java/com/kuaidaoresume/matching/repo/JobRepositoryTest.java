package com.kuaidaoresume.matching.repo;

import com.kuaidaoresume.matching.model.Job;
import com.kuaidaoresume.matching.model.Keyword;
import com.kuaidaoresume.matching.model.Location;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataMongoTest
public class JobRepositoryTest {

    private static final String JOB_UUID = "job uuid";
    private static final String COMPANY_NAME = "company name";
    private static final String MAJOR_A = "major a";
    private static final String MAJOR_B = "major b";
    private static final String MAJOR_C = "major c";
    private static final String KEYWORD = "keyword";
    private static final String KEYWORD1 = "kotlin";
    private static final String KEYWORD2 = "java";
    private static final String KEYWORD3 = "java";
    private static final LocalDateTime POST_DATE = LocalDateTime.of(2020, 8, 1, 0, 0);

    @Autowired
    private JobRepository jobRepository;

    private Location location;
    private Job job;

    private Keyword keyword;
    private Keyword keyword1;
    private Keyword keyword2;
    private Keyword keyword3;

    @BeforeEach
    public void setup() {
        jobRepository.deleteAll();

        keyword = Keyword.builder().value(KEYWORD).rating(1.0).build();
        keyword1 = Keyword.builder().value(KEYWORD1).rating(1.0).build();
        keyword2 = Keyword.builder().value(KEYWORD2).rating(2.0).build();
        keyword3 = Keyword.builder().value(KEYWORD3).rating(3.0).build();

        location = Location.builder()
            .country("Canada")
            .state("BC")
            .city("Vancouver")
            .build();
        job = jobRepository.save(Job.builder()
            .jobUuid(JOB_UUID)
            .companyName(COMPANY_NAME)
            .location(location)
            .relevantMajors(Arrays.asList(MAJOR_A, MAJOR_B, MAJOR_C))
            .keywords(Arrays.asList(keyword))
            .postDate(POST_DATE)
            .createdAt(Instant.now())
            .isActive(true)
            .build());
    }

    @Test
    public void test_matchJobByMajor() {
        List<Job> matchedJobs = jobRepository.findMatchedJobs(
            location.getCountry(), location.getCity(), Arrays.asList(MAJOR_A, MAJOR_C, "another major"), Lists.emptyList());
        assertThat(matchedJobs.size(), is(1));
        assertThat(matchedJobs.get(0), is(job));
    }

    @Test
    public void test_matchJobByKeyword() {
        List<Job> matchedJobs = jobRepository.findMatchedJobs(
            location.getCountry(), location.getCity(), Lists.emptyList(), Arrays.asList(KEYWORD, "another keyword"));
        assertThat(matchedJobs.size(), is(1));
        assertThat(matchedJobs.get(0), is(job));
    }

    @Test
    public void test_shouldNotMatchInactiveJob() {
        jobRepository.save(Job.builder()
            .jobUuid("job uuid 2")
            .companyName(COMPANY_NAME)
            .location(location)
            .relevantMajors(Arrays.asList(MAJOR_A, MAJOR_B, MAJOR_C))
            .keywords(Arrays.asList(keyword1, keyword2, keyword3))
            .postDate(POST_DATE)
            .createdAt(Instant.now())
            .isActive(false)
            .build());
        List<Job> matchedJobs = jobRepository.findMatchedJobs(
            location.getCountry(), location.getCity(), Lists.emptyList(), Arrays.asList(KEYWORD));
        assertThat(matchedJobs.size(), is(1));
        assertThat(matchedJobs.get(0), is(job));
    }

    @Test
    public void test_noMatchedJobs() {
        List<Job> matchedJobs = jobRepository.findMatchedJobs(
            location.getCountry(), location.getCity(), Arrays.asList("another major"), Arrays.asList("keyword4"));
        assertTrue(matchedJobs.isEmpty());
    }

    @Test
    public void test_shouldnMatchJobsSortByPostDateDesc() {
        Job job1 = jobRepository.save(Job.builder()
            .jobUuid("job uuid 1")
            .companyName(COMPANY_NAME)
            .location(location)
            .relevantMajors(Arrays.asList(MAJOR_A, MAJOR_B, MAJOR_C))
            .keywords(Arrays.asList(keyword1, keyword2, keyword3))
            .postDate(LocalDateTime.of(2020, 7, 1, 0, 0))
            .createdAt(Instant.now())
            .isActive(true)
            .build());

        Job job2 = jobRepository.save(Job.builder()
            .jobUuid("job uuid 2")
            .companyName(COMPANY_NAME)
            .location(location)
            .relevantMajors(Arrays.asList(MAJOR_A, MAJOR_B, MAJOR_C))
            .keywords(Arrays.asList(keyword1, keyword2, keyword3))
            .postDate(LocalDateTime.of(2020, 9, 1, 0, 0))
            .createdAt(Instant.now())
            .isActive(true)
            .build());
        List<Job> matchedJobs = jobRepository.findMatchedJobs(
            location.getCountry(), location.getCity(), Lists.emptyList(), Arrays.asList(KEYWORD, KEYWORD2));

        assertThat(matchedJobs.size(), is(3));
        assertThat(matchedJobs.get(0), is(job2));
        assertThat(matchedJobs.get(1), is(job));
        assertThat(matchedJobs.get(2), is(job1));
    }

    @Test
    public void test_findTopByOrderCreatedAtDesc() {
        jobRepository.save(Job.builder()
            .jobUuid("job uuid 1")
            .companyName(COMPANY_NAME)
            .location(location)
            .relevantMajors(Arrays.asList(MAJOR_A, MAJOR_B, MAJOR_C))
            .keywords(Arrays.asList(keyword1, keyword2, keyword3))
            .postDate(LocalDateTime.of(2020, 7, 1, 0, 0))
            .createdAt(Instant.ofEpochMilli(1598230435))
            .isActive(true)
            .build());

        jobRepository.save(Job.builder()
            .jobUuid("job uuid 2")
            .companyName(COMPANY_NAME)
            .location(location)
            .relevantMajors(Arrays.asList(MAJOR_A, MAJOR_B, MAJOR_C))
            .keywords(Arrays.asList(keyword1, keyword2, keyword3))
            .postDate(LocalDateTime.of(2020, 9, 1, 0, 0))
            .createdAt(Instant.ofEpochMilli(1598230436))
            .isActive(true)
            .build());

        Job lastJob = jobRepository.findTopByOrderByCreatedAtDesc().get();

        assertThat(lastJob, is(job));
    }

    @Test
    public void test_searchJobs_termInTitle() {
        Job job1 =  jobRepository.save(Job.builder()
            .jobUuid("job uuid 1")
            .title("Software Engineer")
            .companyName("Google Inc")
            .location(location)
            .relevantMajors(Arrays.asList(MAJOR_A, MAJOR_B, MAJOR_C))
            .keywords(Arrays.asList(keyword1, keyword2, keyword3))
            .postDate(LocalDateTime.of(2020, 7, 1, 0, 0))
            .createdAt(Instant.ofEpochMilli(1598230435))
            .isActive(true)
            .build());
        Job job2 =  jobRepository.save(Job.builder()
            .jobUuid("job uuid 2")
            .title("CTO")
            .companyName("Microsoft")
            .location(location)
            .relevantMajors(Arrays.asList(MAJOR_A, MAJOR_B, MAJOR_C))
            .keywords(Arrays.asList(keyword1, keyword2, keyword3))
            .postDate(LocalDateTime.of(2020, 7, 1, 0, 0))
            .createdAt(Instant.ofEpochMilli(1598230435))
            .isActive(true)
            .build());
        Job job3 =  jobRepository.save(Job.builder()
            .jobUuid("job uuid 3")
            .title("Software Developer")
            .companyName("Microsoft")
            .location(location)
            .relevantMajors(Arrays.asList(MAJOR_A, MAJOR_B, MAJOR_C))
            .keywords(Arrays.asList(keyword1, keyword2, keyword3))
            .postDate(LocalDateTime.of(2020, 7, 1, 0, 0))
            .createdAt(Instant.ofEpochMilli(1598230435))
            .isActive(true)
            .build());
        assertThat(jobRepository.searchJobs(location.getCountry(), location.getCity(), "software", "en"),
            is(Arrays.asList(job1, job3)));
    }

    @Test
    public void test_searchJobs_sortByPostDay() {
        Job job1 =  jobRepository.save(Job.builder()
            .jobUuid("job uuid 1")
            .title("Software Developer")
            .companyName("Google Inc")
            .location(location)
            .relevantMajors(Arrays.asList(MAJOR_A, MAJOR_B, MAJOR_C))
            .keywords(Arrays.asList(keyword1, keyword2, keyword3))
            .postDate(LocalDateTime.of(2020, 7, 2, 0, 0))
            .createdAt(Instant.ofEpochMilli(1598230435))
            .isActive(true)
            .build());
        Job job2 =  jobRepository.save(Job.builder()
            .jobUuid("job uuid 2")
            .title("Software Developer")
            .companyName("Microsoft")
            .location(location)
            .relevantMajors(Arrays.asList(MAJOR_A, MAJOR_B, MAJOR_C))
            .keywords(Arrays.asList(keyword1, keyword2, keyword3))
            .postDate(LocalDateTime.of(2020, 7, 3, 0, 0))
            .createdAt(Instant.ofEpochMilli(1598230435))
            .isActive(true)
            .build());
        Job job3 =  jobRepository.save(Job.builder()
            .jobUuid("job uuid 3")
            .title("Software Developer")
            .companyName("Microsoft")
            .location(location)
            .relevantMajors(Arrays.asList(MAJOR_A, MAJOR_C))
            .keywords(Arrays.asList(keyword1, keyword3))
            .postDate(LocalDateTime.of(2020, 7, 1, 0, 0))
            .createdAt(Instant.ofEpochMilli(1598230435))
            .isActive(true)
            .build());
        assertThat(jobRepository.searchJobs(location.getCountry(), location.getCity(), "software", "en"),
            is(Arrays.asList(job2, job1, job3)));
    }

    @Test
    public void test_searchJobs_sortByMatchingScoreTakePrecedence() {
        Job job1 =  jobRepository.save(Job.builder()
            .jobUuid("job uuid 1")
            .title("Software Engineer")
            .companyName("Google Inc")
            .location(location)
            .relevantMajors(Arrays.asList(MAJOR_A, MAJOR_B, MAJOR_C))
            .keywords(Arrays.asList(keyword1, keyword2, keyword3))
            .postDate(LocalDateTime.of(2020, 7, 1, 0, 0))
            .createdAt(Instant.ofEpochMilli(1598230435))
            .isActive(true)
            .build());
        Job job2 =  jobRepository.save(Job.builder()
            .jobUuid("job uuid 2")
            .title("Software Developer")
            .companyName("Microsoft")
            .location(location)
            .relevantMajors(Arrays.asList(MAJOR_A, MAJOR_B, MAJOR_C))
            .keywords(Arrays.asList(keyword1, keyword2, keyword3))
            .postDate(LocalDateTime.of(2020, 7, 2, 0, 0))
            .createdAt(Instant.ofEpochMilli(1598230435))
            .isActive(true)
            .build());
        assertThat(jobRepository.searchJobs(location.getCountry(), location.getCity(), "software engineer", "en"),
            is(Arrays.asList(job1, job2)));
    }

    @Test
    public void test_searchJobs_unmatchedLocationNotIncluded() {
        Location otherLocation = Location.builder()
            .city("US")
            .city("Seattle")
            .build();
        Job job1 =  jobRepository.save(Job.builder()
            .jobUuid("job uuid 1")
            .title("Software Engineer")
            .companyName("Google Inc")
            .location(otherLocation)
            .relevantMajors(Arrays.asList(MAJOR_A, MAJOR_B, MAJOR_C))
            .keywords(Arrays.asList(keyword1, keyword2, keyword3))
            .postDate(LocalDateTime.of(2020, 7, 1, 0, 0))
            .createdAt(Instant.ofEpochMilli(1598230435))
            .isActive(true)
            .build());
        Job job2 =  jobRepository.save(Job.builder()
            .jobUuid("job uuid 2")
            .title("Software Engineer")
            .companyName("Microsoft")
            .location(location)
            .relevantMajors(Arrays.asList(MAJOR_A, MAJOR_B, MAJOR_C))
            .keywords(Arrays.asList(keyword1, keyword2, keyword3))
            .postDate(LocalDateTime.of(2020, 7, 2, 0, 0))
            .createdAt(Instant.ofEpochMilli(1598230435))
            .isActive(true)
            .build());
        assertThat(jobRepository.searchJobs(location.getCountry(), location.getCity(), "software engineer", "en"),
            is(Arrays.asList(job2)));
    }

    @Test
    public void test_searchJobs_inactiveJobNotIncluded() {
        Job job1 =  jobRepository.save(Job.builder()
            .jobUuid("job uuid 1")
            .title("Software Engineer")
            .companyName("Google Inc")
            .location(location)
            .relevantMajors(Arrays.asList(MAJOR_A, MAJOR_B, MAJOR_C))
            .keywords(Arrays.asList(keyword1, keyword2, keyword3))
            .postDate(LocalDateTime.of(2020, 7, 1, 0, 0))
            .createdAt(Instant.ofEpochMilli(1598230435))
            .isActive(false)
            .build());
        Job job2 =  jobRepository.save(Job.builder()
            .jobUuid("job uuid 2")
            .title("Software Engineer")
            .companyName("Microsoft")
            .location(location)
            .relevantMajors(Arrays.asList(MAJOR_A, MAJOR_B, MAJOR_C))
            .keywords(Arrays.asList(keyword1, keyword2, keyword3))
            .postDate(LocalDateTime.of(2020, 7, 2, 0, 0))
            .createdAt(Instant.ofEpochMilli(1598230435))
            .isActive(true)
            .build());
        assertThat(jobRepository.searchJobs(location.getCountry(), location.getCity(), "software engineer", "en"),
            is(Arrays.asList(job2)));
    }

    @Test
    public void test_searchJobs_termInCompanyName() {
        Job job1 =  jobRepository.save(Job.builder()
            .jobUuid("job uuid 1")
            .companyName("Google Inc")
            .location(location)
            .relevantMajors(Arrays.asList(MAJOR_A, MAJOR_B, MAJOR_C))
            .keywords(Arrays.asList(keyword1, keyword2, keyword3))
            .postDate(LocalDateTime.of(2020, 7, 1, 0, 0))
            .createdAt(Instant.ofEpochMilli(1598230435))
            .isActive(true)
            .build());
        Job job2 =  jobRepository.save(Job.builder()
            .jobUuid("job uuid 2")
            .title("Software Engineer")
            .companyName("Microsoft")
            .location(location)
            .relevantMajors(Arrays.asList(MAJOR_A, MAJOR_B, MAJOR_C))
            .keywords(Arrays.asList(keyword1, keyword2, keyword3))
            .postDate(LocalDateTime.of(2020, 7, 2, 0, 0))
            .createdAt(Instant.ofEpochMilli(1598230435))
            .isActive(true)
            .build());
        assertThat(jobRepository.searchJobs(location.getCountry(), location.getCity(), "google", "en"),
            is(Arrays.asList(job1)));
    }

    @Test
    public void test_searchJobs_termInMajors() {
        Job job1 =  jobRepository.save(Job.builder()
            .jobUuid("job uuid 1")
            .companyName("Google Inc")
            .location(location)
            .relevantMajors(Arrays.asList("cs", "accounting"))
            .keywords(Arrays.asList(keyword1, keyword2, keyword3))
            .postDate(LocalDateTime.of(2020, 7, 1, 0, 0))
            .createdAt(Instant.ofEpochMilli(1598230435))
            .isActive(true)
            .build());
        Job job2 =  jobRepository.save(Job.builder()
            .jobUuid("job uuid 2")
            .title("Software Engineer")
            .companyName("Microsoft")
            .location(location)
            .relevantMajors(Arrays.asList("finance", "law"))
            .keywords(Arrays.asList(keyword1, keyword2, keyword3))
            .postDate(LocalDateTime.of(2020, 7, 2, 0, 0))
            .createdAt(Instant.ofEpochMilli(1598230435))
            .isActive(true)
            .build());
        assertThat(jobRepository.searchJobs(location.getCountry(), location.getCity(), "law", "en"),
            is(Arrays.asList(job2)));
    }

    @Test
    public void test_searchJobs_termInKeywords() {
        Job job1 =  jobRepository.save(Job.builder()
            .jobUuid("job uuid 1")
            .companyName("Google Inc")
            .location(location)
            .relevantMajors(Arrays.asList(MAJOR_A, MAJOR_B, MAJOR_C))
            .keywords(Arrays.asList(keyword1))
            .postDate(LocalDateTime.of(2020, 7, 1, 0, 0))
            .createdAt(Instant.ofEpochMilli(1598230435))
            .isActive(true)
            .build());
        Job job2 =  jobRepository.save(Job.builder()
            .jobUuid("job uuid 2")
            .title("Software Engineer")
            .companyName("Microsoft")
            .location(location)
            .relevantMajors(Arrays.asList(MAJOR_A, MAJOR_B, MAJOR_C))
            .keywords(Arrays.asList(keyword3))
            .postDate(LocalDateTime.of(2020, 7, 2, 0, 0))
            .createdAt(Instant.ofEpochMilli(1598230435))
            .isActive(true)
            .build());
        assertThat(jobRepository.searchJobs(location.getCountry(), location.getCity(), keyword1.getValue(), "en"),
            is(Arrays.asList(job1)));
    }

    @Test
    public void test_searchJobs_termInKeywords_sortByRating() {
        Job job1 =  jobRepository.save(Job.builder()
            .jobUuid("job uuid 1")
            .companyName("Google Inc")
            .location(location)
            .relevantMajors(Arrays.asList(MAJOR_A, MAJOR_B, MAJOR_C))
            .keywords(Arrays.asList(keyword2))
            .postDate(LocalDateTime.of(2020, 7, 2, 0, 0))
            .createdAt(Instant.ofEpochMilli(1598230435))
            .isActive(true)
            .build());
        Job job2 =  jobRepository.save(Job.builder()
            .jobUuid("job uuid 2")
            .title("Software Engineer")
            .companyName("Microsoft")
            .location(location)
            .relevantMajors(Arrays.asList(MAJOR_A, MAJOR_B, MAJOR_C))
            .keywords(Arrays.asList(keyword3))
            .postDate(LocalDateTime.of(2020, 7, 1, 0, 0))
            .createdAt(Instant.ofEpochMilli(1598230435))
            .isActive(true)
            .build());
        assertThat(jobRepository.searchJobs(location.getCountry(), location.getCity(), keyword2.getValue(), "en"),
            is(Arrays.asList(job2, job1)));
    }

    @Test
    public void test_searchJobs_sortByWeights() {
        String term = "blah blah";
        Job job1 =  jobRepository.save(Job.builder()
            .jobUuid("job uuid 1")
            .companyName(term)
            .location(location)
            .relevantMajors(Arrays.asList(MAJOR_A, MAJOR_B, MAJOR_C))
            .keywords(Arrays.asList(keyword2))
            .postDate(LocalDateTime.of(2020, 7, 2, 0, 0))
            .createdAt(Instant.ofEpochMilli(1598230435))
            .isActive(true)
            .build());
        Job job2 =  jobRepository.save(Job.builder()
            .jobUuid("job uuid 2")
            .title(term)
            .companyName("Microsoft")
            .location(location)
            .relevantMajors(Arrays.asList(MAJOR_A, MAJOR_B, MAJOR_C))
            .keywords(Arrays.asList(keyword3))
            .postDate(LocalDateTime.of(2020, 7, 2, 0, 0))
            .createdAt(Instant.ofEpochMilli(1598230435))
            .isActive(true)
            .build());
        Job job3 =  jobRepository.save(Job.builder()
            .jobUuid("job uuid 3")
            .title("Software Engineer")
            .companyName("Microsoft")
            .location(location)
            .relevantMajors(Arrays.asList(MAJOR_A, term))
            .keywords(Arrays.asList(keyword3))
            .postDate(LocalDateTime.of(2020, 7, 3, 0, 0))
            .createdAt(Instant.ofEpochMilli(1598230435))
            .isActive(true)
            .build());
        assertThat(jobRepository.searchJobs(location.getCountry(), location.getCity(), term, "en"),
            is(Arrays.asList(job2, job1, job3)));
    }

    @Test
    public void test_searchJobs_withLimit() {
        Job job1 =  jobRepository.save(Job.builder()
            .jobUuid("job uuid 1")
            .title("Software Engineer")
            .companyName("Google Inc")
            .location(location)
            .relevantMajors(Arrays.asList(MAJOR_A, MAJOR_B, MAJOR_C))
            .keywords(Arrays.asList(keyword1, keyword2, keyword3))
            .postDate(LocalDateTime.of(2020, 7, 1, 0, 0))
            .createdAt(Instant.ofEpochMilli(1598230435))
            .isActive(true)
            .build());
        Job job2 =  jobRepository.save(Job.builder()
            .jobUuid("job uuid 2")
            .title("Software Developer")
            .companyName("Microsoft")
            .location(location)
            .relevantMajors(Arrays.asList(MAJOR_A, MAJOR_B, MAJOR_C))
            .keywords(Arrays.asList(keyword1, keyword2, keyword3))
            .postDate(LocalDateTime.of(2020, 7, 1, 0, 0))
            .createdAt(Instant.ofEpochMilli(1598230435))
            .isActive(true)
            .build());
        Job job3 =  jobRepository.save(Job.builder()
            .jobUuid("job uuid 3")
            .title("Software Developer")
            .companyName("Microsoft")
            .location(location)
            .relevantMajors(Arrays.asList(MAJOR_A, MAJOR_B, MAJOR_C))
            .keywords(Arrays.asList(keyword1, keyword2, keyword3))
            .postDate(LocalDateTime.of(2020, 7, 1, 0, 0))
            .createdAt(Instant.ofEpochMilli(1598230435))
            .isActive(true)
            .build());
        assertThat(jobRepository.searchJobs(location.getCountry(), location.getCity(), "software", "en", 0, 1),
            is(Arrays.asList(job1)));
    }

    @Test
    public void test_searchJobs_withPaging() {
        Job job1 =  jobRepository.save(Job.builder()
            .jobUuid("job uuid 1")
            .title("Software Engineer")
            .companyName("Google Inc")
            .location(location)
            .relevantMajors(Arrays.asList(MAJOR_A, MAJOR_B, MAJOR_C))
            .keywords(Arrays.asList(keyword1, keyword2, keyword3))
            .postDate(LocalDateTime.of(2020, 7, 1, 0, 0))
            .createdAt(Instant.ofEpochMilli(1598230435))
            .isActive(true)
            .build());
        Job job2 =  jobRepository.save(Job.builder()
            .jobUuid("job uuid 2")
            .title("Software Developer")
            .companyName("Microsoft")
            .location(location)
            .relevantMajors(Arrays.asList(MAJOR_A, MAJOR_B, MAJOR_C))
            .keywords(Arrays.asList(keyword1, keyword2, keyword3))
            .postDate(LocalDateTime.of(2020, 7, 1, 0, 0))
            .createdAt(Instant.ofEpochMilli(1598230435))
            .isActive(true)
            .build());
        Job job3 =  jobRepository.save(Job.builder()
            .jobUuid("job uuid 3")
            .title("Software Developer")
            .companyName("Microsoft")
            .location(location)
            .relevantMajors(Arrays.asList(MAJOR_A, MAJOR_B, MAJOR_C))
            .keywords(Arrays.asList(keyword1, keyword2, keyword3))
            .postDate(LocalDateTime.of(2020, 7, 1, 0, 0))
            .createdAt(Instant.ofEpochMilli(1598230435))
            .isActive(true)
            .build());
        assertThat(jobRepository.searchJobs(location.getCountry(), location.getCity(), "software", "en", 1, 2),
            is(Arrays.asList(job3)));
    }
}
