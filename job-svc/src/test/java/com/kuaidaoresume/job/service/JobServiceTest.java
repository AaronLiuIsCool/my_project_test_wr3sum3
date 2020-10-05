package com.kuaidaoresume.job.service;

import com.kuaidaoresume.job.config.CacheConfig;
import com.kuaidaoresume.job.config.JobApplicationTestConfig;
import com.kuaidaoresume.job.dto.*;
import org.modelmapper.ModelMapper;
import org.springframework.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Date;
import java.util.UUID;
import java.util.Arrays;
import java.util.Optional;
import java.util.Collections;

@Import(JobApplicationTestConfig.class)
@ExtendWith(SpringExtension.class)
public class JobServiceTest {
    private static final long id = 1L;
    private static final String uuid = UUID.randomUUID().toString();
    private static final Date postDate = new Date(System.currentTimeMillis());
    private static final String positionTitle = "SDE";
    private static final String companyName = "ABC";
    private static final String url = "someUrl";
    private static final String country = "aCountry";
    private static final String city = "aCity";
    private static final String postCode = "aPostCode";
    private static final String majorName = "CS";
    private static JobDto jobDto;
    private static List<MajorDto> majors;
    private static LocationDto locationDto;
    private static MajorDto majorDto;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    CacheManager cacheManager;

    @MockBean
    JobService jobService;

    @BeforeEach
    void setUp() {
        locationDto = LocationDto.builder()
                .country(country)
                .city(city)
                .postCode(postCode)
                .build();

        majorDto = MajorDto.builder()
                .name(majorName)
                .build();

        majors = Arrays.asList(majorDto);

        jobDto = JobDto.builder()
                .uuid(uuid)
                .postDate(postDate)
                .positionTitle(positionTitle)
                .companyName(companyName)
                .url(url)
                .location(locationDto)
                .majors(majors)
                .build();
        jobService.createJob(jobDto);
    }

    private Optional<JobDto> getCachedJob(String uuid) {
        return Optional.ofNullable(cacheManager.getCache(CacheConfig.JOB_CACHE)).map(c -> c.get(uuid, JobDto.class));
    }

    private Optional<JobDto> getCachedJob(long id) {
        return Optional.ofNullable(cacheManager.getCache(CacheConfig.JOB_CACHE)).map(c -> c.get(id, JobDto.class));
    }

    private List<JobDto> getCachedJob(LocationDto locationDto) {
        return Optional.ofNullable(cacheManager.getCache(CacheConfig.JOB_SEARCH_CACHE))
                .map(c -> c.get(locationDto, JobDto.class)).map(Collections::singletonList)
                .orElseGet(Collections::emptyList);
    }

    private List<JobDto> getCachedJob(MajorDto majorDto) {
        return Optional.ofNullable(cacheManager.getCache(CacheConfig.JOB_SEARCH_CACHE))
                .map(c -> c.get(majorDto, JobDto.class)).map(Collections::singletonList)
                .orElseGet(Collections::emptyList);
    }

    @Test
    public void givenJobThatShouldBeCached_whenFindByUuid_thenResultShouldBePutInCache() {
        Optional<JobDto> jobDto = jobService.findJobByUuid(uuid);
        assertEquals(jobDto, getCachedJob(uuid));
    }

    @Test
    public void givenJobThatShouldNotBeCached_whenFindByUuid_thenResultShouldNotBePutInCache() {
        assertEquals(Optional.empty(), getCachedJob("not uuid"));
    }

    @Test
    public void givenJobThatShouldBeCached_whenFindById_thenResultShouldBePutInCache() {
        Optional<JobDto> jobDto = jobService.findJobById(id);
        assertEquals(jobDto, getCachedJob(id));
    }

    @Test
    public void givenJobThatShouldNotBeCached_whenFindById_thenResultShouldNotBePutInCache() {
        assertEquals(Optional.empty(), getCachedJob(-1));
    }

    @Test
    public void givenJobLocationSearchThatShouldBeCached_whenFindById_thenResultShouldBePutInCache() {
        List<JobDto> jobDtoList = jobService.findJobByLocation(Arrays.asList(locationDto));
        assertEquals(jobDtoList, getCachedJob(locationDto));
    }

    @Test
    public void givenJobMajorSearchThatShouldBeCached_whenFindById_thenResultShouldBePutInCache() {
        List<JobDto> jobDtoList = jobService.findJobByMajor(Arrays.asList(majorDto));
        assertEquals(jobDtoList, getCachedJob(majorDto));
    }

    @Test
    public void givenJobLocationMajorSearchThatShouldBeCached_whenFindById_thenResultShouldBePutInCache() {
        List<JobDto> jobDtoList = jobService.findJobByLocationAndMajor(Arrays.asList(locationDto), Arrays.asList(majorDto));
        assertEquals(jobDtoList, getCachedJob(majorDto));
    }
}
