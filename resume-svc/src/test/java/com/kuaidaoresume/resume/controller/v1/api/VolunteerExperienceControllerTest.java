package com.kuaidaoresume.resume.controller.v1.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuaidaoresume.resume.config.ResumeApplicationTestConfig;
import com.kuaidaoresume.resume.controller.v1.assembler.VolunteerExperienceRepresentationModelAssembler;
import com.kuaidaoresume.resume.dto.ExperienceDto;
import com.kuaidaoresume.resume.dto.PersistedVolunteerExperienceDto;
import com.kuaidaoresume.resume.model.VolunteerExperience;
import com.kuaidaoresume.resume.service.ResumeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
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

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(VolunteerExperienceController.class)
@Import({ VolunteerExperienceRepresentationModelAssembler.class, ResumeApplicationTestConfig.class })
public class VolunteerExperienceControllerTest {

    private static final String RESUME_ID = "aUUID";
    private static final Long VOLUNTEER_EXPERIENCE_ID = 1L;
    private static final String ROLE = "CEO";
    private static final String ORGANIZATION = "Delos Inc.";
    private static final String COUNTRY = "Canada";
    private static final String CITY = "Vancouver";
    private static final String DESCRIPTION = "I nailed it.";
    private static final LocalDate START_DATE = LocalDate.of(2000, 1, 1);
    private static final LocalDate END_DATE = LocalDate.of(2001, 1, 1);
    private static final String START_DATE_TEXT = START_DATE.toString();
    private static final String END_DATE_TEXT = END_DATE.toString();

    private VolunteerExperience volunteerExperience;

    private ExperienceDto experienceDto;

    private PersistedVolunteerExperienceDto persistedVolunteerExperienceDto;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private ResumeService resumeService;

    @BeforeEach
    public void setup() {
        experienceDto = ExperienceDto.builder()
            .role(ROLE)
            .organization(ORGANIZATION)
            .city(CITY)
            .country(COUNTRY)
            .startDate(START_DATE_TEXT)
            .endDate(END_DATE_TEXT)
            .description(DESCRIPTION)
            .build();

        persistedVolunteerExperienceDto = modelMapper.map(experienceDto, PersistedVolunteerExperienceDto.class);
        persistedVolunteerExperienceDto.setId(VOLUNTEER_EXPERIENCE_ID);

        volunteerExperience = VolunteerExperience.builder()
            .id(VOLUNTEER_EXPERIENCE_ID)
            .role(ROLE)
            .organization(ORGANIZATION)
            .city(CITY)
            .country(COUNTRY)
            .startDate(START_DATE)
            .endDate(END_DATE)
            .description(DESCRIPTION)
            .build();
    }

    @Test
    public void whenFindById_thenReturn200() throws Exception {
        given(resumeService.findById(VOLUNTEER_EXPERIENCE_ID, VolunteerExperience.class)).willReturn(Optional.of(volunteerExperience));

        mvc.perform(get("/v1/volunteer-experiences/{id}", VOLUNTEER_EXPERIENCE_ID).accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()))
            .andExpect(jsonPath("$.role", is(ROLE)))
            .andExpect(jsonPath("$.organization", is(ORGANIZATION)))
            .andExpect(jsonPath("$.city", is(CITY)))
            .andExpect(jsonPath("$.country", is(COUNTRY)))
            .andExpect(jsonPath("$.startDate", is(START_DATE_TEXT)))
            .andExpect(jsonPath("$.endDate", is(END_DATE_TEXT)))
            .andExpect(jsonPath("$.description", is(DESCRIPTION)))
            .andExpect(jsonPath("$.links[0].rel", is("self")))
            .andExpect(jsonPath("$.links[0].href", is(String.format("http://localhost/v1/volunteer-experiences/%s", VOLUNTEER_EXPERIENCE_ID))))
            .andReturn();
    }

    @Test
    public void whenFindAllByResumeId_thenReturn200() throws Exception {
        given(resumeService.findAllByResumeId(RESUME_ID, VolunteerExperience.class)).willReturn(Arrays.asList(volunteerExperience));
        mvc.perform(get("/v1/resumes/{resumeId}/volunteer-experiences", RESUME_ID).accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()))
            .andExpect(jsonPath("$.content[0].role", is(ROLE)))
            .andExpect(jsonPath("$.content[0].organization", is(ORGANIZATION)))
            .andExpect(jsonPath("$.content[0].country", is(COUNTRY)))
            .andExpect(jsonPath("$.content[0].city", is(CITY)))
            .andExpect(jsonPath("$.content[0].startDate", is(START_DATE_TEXT)))
            .andExpect(jsonPath("$.content[0].endDate", is(END_DATE_TEXT)))
            .andExpect(jsonPath("$.content[0].description", is(DESCRIPTION)))
            .andExpect(jsonPath("$.links[0].rel", is("self")))
            .andExpect(jsonPath("$.links[0].href", is(String.format("http://localhost/v1/resumes/%s/volunteer-experiences", RESUME_ID))))
            .andReturn();
    }

    @Test
    public void whenCreate_thenReturn201() throws Exception {
        given(resumeService.newVolunteerExperience(eq(RESUME_ID), any(VolunteerExperience.class))).willReturn(volunteerExperience);

        mvc.perform(post("/v1/resumes/{resumeId}/volunteer-experiences", RESUME_ID)
            .content(objectMapper.writeValueAsString(experienceDto))
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()))
            .andReturn();
    }

    @Test
    public void whenSave_thenReturn202() throws Exception {
        doNothing().when(resumeService).updateResumeContainable(volunteerExperience, VolunteerExperience.class);

        mvc.perform(put("/v1/volunteer-experiences/{id}", VOLUNTEER_EXPERIENCE_ID)
            .content(objectMapper.writeValueAsString(persistedVolunteerExperienceDto))
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNoContent())
            .andReturn();
    }

    @Test
    public void whenDeleteById_thenReturn202() throws Exception {
        doNothing().when(resumeService).deleteById(VOLUNTEER_EXPERIENCE_ID, VolunteerExperience.class, RESUME_ID);

        mvc.perform(delete("/v1/volunteer-experiences/{id}?resumeId={resumeId}", VOLUNTEER_EXPERIENCE_ID, RESUME_ID))
            .andDo(print())
            .andExpect(status().isNoContent())
            .andReturn();
    }

    @Test
    public void whenDeleteAllByResumeId_thenReturn202() throws Exception {
        doNothing().when(resumeService).deleteAllByResumeId(RESUME_ID, VolunteerExperience.class);

        mvc.perform(delete("/v1/resumes/{resumeId}/volunteer-experiences", RESUME_ID))
            .andDo(print())
            .andExpect(status().isNoContent())
            .andReturn();
    }
}
