package com.kuaidaoresume.job.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuaidaoresume.common.auth.AuthConstant;
import com.kuaidaoresume.job.config.JobApplicationTestConfig;
import com.kuaidaoresume.job.controller.assembler.RatingRepresentationModelAssembler;
import com.kuaidaoresume.job.dto.*;
import com.kuaidaoresume.job.model.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import com.kuaidaoresume.job.service.RatingService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.modelmapper.ModelMapper;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@Import({ RatingRepresentationModelAssembler.class, JobApplicationTestConfig.class })
@ExtendWith(SpringExtension.class)
@WebMvcTest(RatingController.class)
public class RatingControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private RatingService ratingService;


    @Test
    public void whenCreateJobRating_thenReturn201() throws Exception {
        Job job = Job.builder()
                .id(1L)
                .postDate(new Date(System.currentTimeMillis()))
                .positionTitle("SDE")
                .companyName("ABC")
                .url("someUrl")
                .build();

        Keyword keyword = Keyword.builder()
                                .id(2L)
                                .name("aKeyword")
                                .build();

        JobHasKeyword rating = JobHasKeyword.builder()
                                            .job(job)
                                            .keyword(keyword)
                                            .rating(11.0)
                                            .build();

        when(ratingService.addJobRating(any(JobHasKeywordDto.class))).thenReturn(Optional.of(modelMapper.map(rating, JobHasKeywordDto.class)));

        mvc.perform(post("/v1/ratings/job").header(HttpHeaders.AUTHORIZATION, AuthConstant.AUTHORIZATION_SUPPORT_USER)
                .content(objectMapper.writeValueAsString(modelMapper.map(rating, JobHasKeywordDto.class)))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        verify(ratingService, times(1)).addJobRating(any(JobHasKeywordDto.class));
        verifyNoMoreInteractions(ratingService);
    }


    @Test
    public void whenUpdateJobRating_thenReturn204() throws Exception {
        Job job = Job.builder()
                .id(1L)
                .postDate(new Date(System.currentTimeMillis()))
                .positionTitle("SDE")
                .companyName("ABC")
                .url("someUrl")
                .build();

        Keyword keyword = Keyword.builder()
                .id(2L)
                .name("aKeyword")
                .build();

        JobHasKeyword rating = JobHasKeyword.builder()
                .job(job)
                .keyword(keyword)
                .rating(11.0)
                .build();

        when(ratingService.updateJobRating(any(JobHasKeywordDto.class))).thenReturn(Optional.of(modelMapper.map(rating, JobHasKeywordDto.class)));

        mvc.perform(put("/v1/ratings/job").header(HttpHeaders.AUTHORIZATION, AuthConstant.AUTHORIZATION_SUPPORT_USER)
                .content(objectMapper.writeValueAsString(modelMapper.map(rating, JobHasKeywordDto.class)))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();

        verify(ratingService, times(1)).updateJobRating(any(JobHasKeywordDto.class));
        verifyNoMoreInteractions(ratingService);
    }

    @Test
    public void whenDeleteJobRating_thenReturn201() throws Exception {
        Job job = Job.builder()
                .id(1L)
                .postDate(new Date(System.currentTimeMillis()))
                .positionTitle("SDE")
                .companyName("ABC")
                .url("someUrl")
                .build();

        Keyword keyword = Keyword.builder()
                .id(2L)
                .name("aKeyword")
                .build();

        JobHasKeyword rating = JobHasKeyword.builder()
                .job(job)
                .keyword(keyword)
                .rating(11.0)
                .build();

        when(ratingService.deleteJobRating(any(JobHasKeywordDto.class))).thenReturn(Optional.of(modelMapper.map(rating, JobHasKeywordDto.class)));

        mvc.perform(delete("/v1/ratings/job").header(HttpHeaders.AUTHORIZATION, AuthConstant.AUTHORIZATION_SUPPORT_USER)
                .content(objectMapper.writeValueAsString(modelMapper.map(rating, JobHasKeywordDto.class)))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andReturn();

        verify(ratingService, times(1)).deleteJobRating(any(JobHasKeywordDto.class));
        verifyNoMoreInteractions(ratingService);
    }

    @Test
    public void whenCreateLocationRating_thenReturn201() throws Exception {
        Location location = Location.builder()
                .id(1L)
                .country("aCountry")
                .city("aCity")
                .postCode("aPostCode")
                .build();

        Keyword keyword = Keyword.builder()
                .id(2L)
                .name("aKeyword")
                .build();

        LocationHasKeyword rating = LocationHasKeyword.builder()
                .location(location)
                .keyword(keyword)
                .rating(11.0)
                .build();

        when(ratingService.addLocationRating(any(LocationHasKeywordDto.class))).thenReturn(Optional.of(modelMapper.map(rating, LocationHasKeywordDto.class)));

        mvc.perform(post("/v1/ratings/location").header(HttpHeaders.AUTHORIZATION, AuthConstant.AUTHORIZATION_SUPPORT_USER)
                .content(objectMapper.writeValueAsString(modelMapper.map(rating, LocationHasKeywordDto.class)))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        verify(ratingService, times(1)).addLocationRating(any(LocationHasKeywordDto.class));
        verifyNoMoreInteractions(ratingService);
    }


    @Test
    public void whenUpdateLocationRating_thenReturn204() throws Exception {
        Location location = Location.builder()
                .id(1L)
                .country("aCountry")
                .city("aCity")
                .postCode("aPostCode")
                .build();

        Keyword keyword = Keyword.builder()
                .id(2L)
                .name("aKeyword")
                .build();

        LocationHasKeyword rating = LocationHasKeyword.builder()
                .location(location)
                .keyword(keyword)
                .rating(11.0)
                .build();

        when(ratingService.updateLocationRating(any(LocationHasKeywordDto.class))).thenReturn(Optional.of(modelMapper.map(rating, LocationHasKeywordDto.class)));

        mvc.perform(put("/v1/ratings/location").header(HttpHeaders.AUTHORIZATION, AuthConstant.AUTHORIZATION_SUPPORT_USER)
                .content(objectMapper.writeValueAsString(modelMapper.map(rating, LocationHasKeywordDto.class)))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();

        verify(ratingService, times(1)).updateLocationRating(any(LocationHasKeywordDto.class));
        verifyNoMoreInteractions(ratingService);
    }

    @Test
    public void whenDeleteLocationRating_thenReturn201() throws Exception {
        Location location = Location.builder()
                .id(1L)
                .country("aCountry")
                .city("aCity")
                .postCode("aPostCode")
                .build();

        Keyword keyword = Keyword.builder()
                .id(2L)
                .name("aKeyword")
                .build();

        LocationHasKeyword rating = LocationHasKeyword.builder()
                .location(location)
                .keyword(keyword)
                .rating(11.0)
                .build();

        when(ratingService.deleteLocationRating(any(LocationHasKeywordDto.class))).thenReturn(Optional.of(modelMapper.map(rating, LocationHasKeywordDto.class)));

        mvc.perform(delete("/v1/ratings/location").header(HttpHeaders.AUTHORIZATION, AuthConstant.AUTHORIZATION_SUPPORT_USER)
                .content(objectMapper.writeValueAsString(modelMapper.map(rating, LocationHasKeywordDto.class)))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andReturn();

        verify(ratingService, times(1)).deleteLocationRating(any(LocationHasKeywordDto.class));
        verifyNoMoreInteractions(ratingService);
    }


    @Test
    public void whenCreateMajorRating_thenReturn201() throws Exception {
        Major major = Major.builder()
                .id(1L)
                .name("aMajor")
                .build();

        Keyword keyword = Keyword.builder()
                .id(2L)
                .name("aKeyword")
                .build();

        MajorHasKeyword rating = MajorHasKeyword.builder()
                .major(major)
                .keyword(keyword)
                .rating(11.0)
                .build();

        when(ratingService.addMajorRating(any(MajorHasKeywordDto.class))).thenReturn(Optional.of(modelMapper.map(rating, MajorHasKeywordDto.class)));

        mvc.perform(post("/v1/ratings/major").header(HttpHeaders.AUTHORIZATION, AuthConstant.AUTHORIZATION_SUPPORT_USER)
                .content(objectMapper.writeValueAsString(modelMapper.map(rating, MajorHasKeywordDto.class)))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        verify(ratingService, times(1)).addMajorRating(any(MajorHasKeywordDto.class));
        verifyNoMoreInteractions(ratingService);
    }


    @Test
    public void whenUpdateMajorRating_thenReturn204() throws Exception {
        Major major = Major.builder()
                .id(1L)
                .name("aMajor")
                .build();

        Keyword keyword = Keyword.builder()
                .id(2L)
                .name("aKeyword")
                .build();

        MajorHasKeyword rating = MajorHasKeyword.builder()
                .major(major)
                .keyword(keyword)
                .rating(11.0)
                .build();

        when(ratingService.updateMajorRating(any(MajorHasKeywordDto.class))).thenReturn(Optional.of(modelMapper.map(rating, MajorHasKeywordDto.class)));

        mvc.perform(put("/v1/ratings/major").header(HttpHeaders.AUTHORIZATION, AuthConstant.AUTHORIZATION_SUPPORT_USER)
                .content(objectMapper.writeValueAsString(modelMapper.map(rating, MajorHasKeywordDto.class)))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();

        verify(ratingService, times(1)).updateMajorRating(any(MajorHasKeywordDto.class));
        verifyNoMoreInteractions(ratingService);
    }

    @Test
    public void whenDeleteMajorRating_thenReturn201() throws Exception {
        Major major = Major.builder()
                .id(1L)
                .name("aMajor")
                .build();

        Keyword keyword = Keyword.builder()
                .id(2L)
                .name("aKeyword")
                .build();

        MajorHasKeyword rating = MajorHasKeyword.builder()
                .major(major)
                .keyword(keyword)
                .rating(11.0)
                .build();

        when(ratingService.deleteMajorRating(any(MajorHasKeywordDto.class))).thenReturn(Optional.of(modelMapper.map(rating, MajorHasKeywordDto.class)));

        mvc.perform(delete("/v1/ratings/major").header(HttpHeaders.AUTHORIZATION, AuthConstant.AUTHORIZATION_SUPPORT_USER)
                .content(objectMapper.writeValueAsString(modelMapper.map(rating, MajorHasKeywordDto.class)))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andReturn();

        verify(ratingService, times(1)).deleteMajorRating(any(MajorHasKeywordDto.class));
        verifyNoMoreInteractions(ratingService);
    }

}