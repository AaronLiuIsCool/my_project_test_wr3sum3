package com.kuaidaoresume.resume.controller.v1.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuaidaoresume.resume.controller.v1.assembler.EducationRepresentationModelAssembler;
import com.kuaidaoresume.resume.dto.AwardDto;
import com.kuaidaoresume.resume.dto.EducationDto;
import com.kuaidaoresume.resume.dto.PersistedEducationDto;
import com.kuaidaoresume.resume.model.Award;
import com.kuaidaoresume.resume.model.Education;
import com.kuaidaoresume.resume.service.ResumeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EducationController.class)
@Import({ EducationRepresentationModelAssembler.class })
public class EducationControllerTest {

    private static final String RESUME_ID = "aUUID";
    private static final long EDUCATION_ID = 1L;
    private static final String COUNTRY = "Canada";
    private static final String CITY = "Vancouver";
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

    private Education education;

    private EducationDto educationDto;

    private PersistedEducationDto persistedEducationDto;

    private Award award;

    private AwardDto awardDto;

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
        awardDto = new AwardDto(AWARD_NAME);

        educationDto = EducationDto.builder()
            .country(COUNTRY)
            .city(CITY)
            .institution(INSTITUTION)
            .major(MAJOR)
            .degree(DEGREE)
            .gpa(GPA)
            .startDate(START_DATE_TEXT)
            .endDate(END_DATE_TEXT)
            .awards(Arrays.asList(awardDto))
            .build();

        persistedEducationDto = modelMapper.map(educationDto, PersistedEducationDto.class);
        persistedEducationDto.setId(EDUCATION_ID);

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
    }

    @Test
    public void whenFindById_thenReturn200() throws Exception {
        given(resumeService.findEducationById(EDUCATION_ID)).willReturn(Optional.of(education));

        mvc.perform(get("/v1/educations/{id}", EDUCATION_ID).accept(MediaTypes.HAL_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
            .andExpect(jsonPath("$.country", is(COUNTRY)))
            .andExpect(jsonPath("$.city", is(CITY)))
            .andExpect(jsonPath("$.institution", is(INSTITUTION)))
            .andExpect(jsonPath("$.major", is(MAJOR)))
            .andExpect(jsonPath("$.degree", is(DEGREE)))
            .andExpect(jsonPath("$.gpa", is(GPA)))
            .andExpect(jsonPath("$.startDate", is(START_DATE_TEXT)))
            .andExpect(jsonPath("$.endDate", is(END_DATE_TEXT)))
            .andExpect(jsonPath("$._links.self.href", is(String.format("http://localhost/v1/educations/%s", EDUCATION_ID))))
            .andReturn();
    }

    @Test
    public void whenFindAllByResumeId_thenReturn200() throws Exception {
        given(resumeService.findEducationsByResumeId(RESUME_ID)).willReturn(Arrays.asList(education));
        mvc.perform(get("/v1/resumes/{resumeId}/educations", RESUME_ID).accept(MediaTypes.HAL_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
            .andExpect(jsonPath("$._embedded.educations[0].country", is(COUNTRY)))
            .andExpect(jsonPath("$._embedded.educations[0].city", is(CITY)))
            .andExpect(jsonPath("$._embedded.educations[0].institution", is(INSTITUTION)))
            .andExpect(jsonPath("$._embedded.educations[0].major", is(MAJOR)))
            .andExpect(jsonPath("$._embedded.educations[0].degree", is(DEGREE)))
            .andExpect(jsonPath("$._embedded.educations[0].gpa", is(GPA)))
            .andExpect(jsonPath("$._embedded.educations[0].startDate", is(START_DATE_TEXT)))
            .andExpect(jsonPath("$._embedded.educations[0].endDate", is(END_DATE_TEXT)))
            .andExpect(jsonPath("$._links.self.href", is(String.format("http://localhost/v1/resumes/%s/educations", RESUME_ID))))
            .andReturn();
    }

    @Test
    public void whenCreate_thenReturn201() throws Exception {
        given(resumeService.newEducation(eq(RESUME_ID), any(Education.class))).willReturn(education);

        mvc.perform(post("/v1/resumes/{resumeId}/educations", RESUME_ID)
            .content(objectMapper.writeValueAsString(educationDto))
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE))
            .andReturn();
    }

    @Test
    public void whenSave_thenReturn202() throws Exception {
        given(resumeService.saveEducation(any(Education.class))).willReturn(education);

        mvc.perform(put("/v1/educations/{id}", EDUCATION_ID)
            .content(objectMapper.writeValueAsString(persistedEducationDto))
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNoContent())
            .andReturn();
    }

    @Test
    public void whenSaveAll_thenReturn202() throws Exception {
        given(resumeService.saveEducation(any(Education.class))).willReturn(education);

        mvc.perform(put("/v1/resumes/{resumeId}/educations", RESUME_ID)
            .content(objectMapper.writeValueAsString(Arrays.asList(persistedEducationDto)))
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNoContent())
            .andReturn();
    }

    @Test
    public void whenDeleteById_thenReturn202() throws Exception {
        doNothing().when(resumeService).deleteEducationById(EDUCATION_ID);

        mvc.perform(delete("/v1/educations/{id}", EDUCATION_ID))
            .andDo(print())
            .andExpect(status().isNoContent())
            .andReturn();
    }

    @Test
    public void whenDeleteAllByResumeId_thenReturn202() throws Exception {
        doNothing().when(resumeService).deleteAllEducationsByResumeId(RESUME_ID);

        mvc.perform(delete("/v1/resumes/{resumeId}/educations", RESUME_ID))
            .andDo(print())
            .andExpect(status().isNoContent())
            .andReturn();
    }
}
