package com.kuaidaoresume.resume.controller.v1.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuaidaoresume.resume.config.ResumeApplicationTestConfig;
import com.kuaidaoresume.resume.controller.v1.assembler.WorkExperienceRepresentationModelAssembler;
import com.kuaidaoresume.resume.dto.ExperienceDto;
import com.kuaidaoresume.resume.dto.PersistedWorkExperienceDto;
import com.kuaidaoresume.resume.model.WorkExperience;
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
@WebMvcTest(WorkExperienceController.class)
@Import({ WorkExperienceRepresentationModelAssembler.class, ResumeApplicationTestConfig.class })
public class WorkExperienceControllerTest {

    private static final String RESUME_ID = "aUUID";
    private static final Long WORK_EXPERIENCE_ID = 1L;
    private static final String ROLE = "CEO";
    private static final String ORGANIZATION = "Delos Inc.";
    private static final String COUNTRY = "Canada";
    private static final String CITY = "Vancouver";
    private static final String DESCRIPTION = "I nailed it.";
    private static final Date START_DATE = Date.valueOf(LocalDate.of(2000, 1, 1));
    private static final Date END_DATE = Date.valueOf(LocalDate.of(2001, 1, 1));
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final String START_DATE_TEXT = START_DATE.toLocalDate().format(FORMATTER);
    private static final String END_DATE_TEXT = END_DATE.toLocalDate().format(FORMATTER);

    private WorkExperience workExperience;

    private ExperienceDto experienceDto;

    private PersistedWorkExperienceDto persistedWorkExperienceDto;

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

        persistedWorkExperienceDto = modelMapper.map(experienceDto, PersistedWorkExperienceDto.class);
        persistedWorkExperienceDto.setId(WORK_EXPERIENCE_ID);

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
    }

    @Test
    public void whenFindById_thenReturn200() throws Exception {
        given(resumeService.findById(WORK_EXPERIENCE_ID, WorkExperience.class)).willReturn(Optional.of(workExperience));

        mvc.perform(get("/v1/work-experiences/{id}", WORK_EXPERIENCE_ID).accept(MediaType.APPLICATION_JSON))
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
            .andExpect(jsonPath("$.links[0].href", is(String.format("http://localhost/v1/work-experiences/%s", WORK_EXPERIENCE_ID))))
            .andReturn();
    }

    @Test
    public void whenFindAllByResumeId_thenReturn200() throws Exception {
        given(resumeService.findAllByResumeId(RESUME_ID, WorkExperience.class)).willReturn(Arrays.asList(workExperience));
        mvc.perform(get("/v1/resumes/{resumeId}/work-experiences", RESUME_ID).accept(MediaType.APPLICATION_JSON))
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
            .andExpect(jsonPath("$.links[0].href", is(String.format("http://localhost/v1/resumes/%s/work-experiences", RESUME_ID))))
            .andReturn();
    }

    @Test
    public void whenCreate_thenReturn201() throws Exception {
        given(resumeService.newWorkExperience(eq(RESUME_ID), any(WorkExperience.class))).willReturn(workExperience);

        mvc.perform(post("/v1/resumes/{resumeId}/work-experiences", RESUME_ID)
            .content(objectMapper.writeValueAsString(experienceDto))
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()))
            .andReturn();
    }

    @Test
    public void whenSave_thenReturn202() throws Exception {
        doNothing().when(resumeService).updateResumeContainable(workExperience, WorkExperience.class);

        mvc.perform(put("/v1/work-experiences/{id}", WORK_EXPERIENCE_ID)
            .content(objectMapper.writeValueAsString(persistedWorkExperienceDto))
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNoContent())
            .andReturn();
    }

    @Test
    public void whenDeleteById_thenReturn202() throws Exception {
        doNothing().when(resumeService).deleteById(WORK_EXPERIENCE_ID, WorkExperience.class);

        mvc.perform(delete("/v1/work-experiences/{id}", WORK_EXPERIENCE_ID))
            .andDo(print())
            .andExpect(status().isNoContent())
            .andReturn();
    }

    @Test
    public void whenDeleteAllByResumeId_thenReturn202() throws Exception {
        doNothing().when(resumeService).deleteAllByResumeId(RESUME_ID, WorkExperience.class);

        mvc.perform(delete("/v1/resumes/{resumeId}/work-experiences", RESUME_ID))
            .andDo(print())
            .andExpect(status().isNoContent())
            .andReturn();
    }
}
