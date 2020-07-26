package com.kuaidaoresume.job.dto;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import com.kuaidaoresume.job.dto.*;
import com.kuaidaoresume.job.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;

public class JobDtoTest {
    private static final Long id = 1L;
    private static final Date postDate = new Date(System.currentTimeMillis());
    private static final String positionTitle = "SDE";
    private static final String companyName = "ABC";
    private static final String url = "someUrl";
    private static final String country = "aCountry";
    private static final String city = "aCity";
    private static final String postCode = "aPostCode";
    private static final String majorName = "CS";

    private ModelMapper modelMapper = new ModelMapper();

    @Test
    public void whenConvertModelsToDtos_thenCorrect() {
        List<Major> majors = new ArrayList<>();

        Location location = Location.builder()
                                    .id(id)
                                    .country(country)
                                    .city(city)
                                    .postCode(postCode)
                                    .build();

        Major major = Major.builder()
                            .id(id)
                            .name(majorName)
                            .build();

        majors.add(major);

        Job job = Job.builder()
                .id(id)
                .postDate(postDate)
                .positionTitle(positionTitle)
                .companyName(companyName)                
                .url(url)
                .location(location)
                .majors(majors)
                .build();

        JobDto jobDto = modelMapper.map(job, JobDto.class);
        LocationDto locationDto = modelMapper.map(location, LocationDto.class);       
        MajorDto majorDto = modelMapper.map(major, MajorDto.class);

        PersistedJobDto persistedJobDto = modelMapper.map(jobDto, PersistedJobDto.class);

        assertNotNull(jobDto);
        assertThat(jobDto.getPostDate(), is(postDate));
        assertThat(jobDto.getPositionTitle(), is(positionTitle));
        assertThat(jobDto.getCompanyName(), is(companyName));      
        assertThat(jobDto.getUrl(), is(url));
        assertThat(jobDto.getMajors().get(0), is(majorDto));
        assertThat(jobDto.getLocation(), is(locationDto));

        assertNotNull(persistedJobDto);
        assertThat(persistedJobDto.getPostDate(), is(postDate));
        assertThat(persistedJobDto.getPositionTitle(), is(positionTitle));
        assertThat(persistedJobDto.getCompanyName(), is(companyName));      
        assertThat(persistedJobDto.getUrl(), is(url));
        assertThat(persistedJobDto.getMajors().get(0), is(majorDto));
        assertThat(persistedJobDto.getLocation(), is(locationDto));
    }

    @Test
    public void whenConvertDtosToModels_thenCorrect() {
        List<MajorDto> majorDtos = new ArrayList<>();

        LocationDto locationDto = LocationDto.builder()
                                    .country(country)
                                    .city(city)
                                    .postCode(postCode)
                                    .build();

        MajorDto majorDto = MajorDto.builder()
                            .name(majorName)
                            .build();

        majorDtos.add(majorDto);

        JobDto jobDto = JobDto.builder()
                .postDate(postDate)
                .positionTitle(positionTitle)
                .url(url)
                .location(locationDto)
                .majors(majorDtos)
                .build();

        Job job = modelMapper.map(jobDto, Job.class);
        Location location = modelMapper.map(locationDto, Location.class);       
        Major major = modelMapper.map(majorDto, Major.class);

        assertNotNull(job);
        assertThat(job.getPostDate(), is(postDate));
        assertThat(job.getPositionTitle(), is(positionTitle));
        assertThat(job.getUrl(), is(url));
        assertThat(job.getMajors().get(0), is(major));
        assertThat(job.getLocation(), is(location));
    }
}