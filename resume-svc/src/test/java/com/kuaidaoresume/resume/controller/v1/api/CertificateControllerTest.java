package com.kuaidaoresume.resume.controller.v1.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuaidaoresume.resume.config.ResumeApplicationTestConfig;
import com.kuaidaoresume.resume.controller.v1.assembler.CertificateRepresentationModelAssembler;
import com.kuaidaoresume.resume.dto.CertificateDto;
import com.kuaidaoresume.resume.dto.PersistedCertificateDto;
import com.kuaidaoresume.resume.model.Certificate;
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
@WebMvcTest(CertificateController.class)
@Import({ CertificateRepresentationModelAssembler.class, ResumeApplicationTestConfig.class })
public class CertificateControllerTest {

    private static final String RESUME_ID = "aUUID";
    private static final Long CERTIFICATE_ID = 1L;
    private static final String NAME = "CPA";

    private static final Date ISSUE_DATE = Date.valueOf(LocalDate.of(2000, 1, 1));
    private static final Date EXPIRATION_DATE = Date.valueOf(LocalDate.of(2001, 1, 1));
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final String ISSUE_DATE_TEXT = ISSUE_DATE.toLocalDate().format(FORMATTER);
    private static final String EXPIRATION_DATE_TEXT = EXPIRATION_DATE.toLocalDate().format(FORMATTER);

    private Certificate certificate;

    private CertificateDto certificateDto;

    private PersistedCertificateDto persistedCertificateDto;

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
        certificateDto = CertificateDto.builder()
            .name(NAME)
            .issueDate(ISSUE_DATE_TEXT)
            .expirationDate(EXPIRATION_DATE_TEXT)
            .build();

        persistedCertificateDto = modelMapper.map(certificateDto, PersistedCertificateDto.class);
        persistedCertificateDto.setId(CERTIFICATE_ID);

        certificate = Certificate.builder()
            .id(CERTIFICATE_ID)
            .name(NAME)
            .issueDate(ISSUE_DATE)
            .expirationDate(EXPIRATION_DATE)
            .build();
    }

    @Test
    public void whenFindById_thenReturn200() throws Exception {
        given(resumeService.findById(CERTIFICATE_ID, Certificate.class)).willReturn(Optional.of(certificate));

        mvc.perform(get("/v1/certificates/{id}", CERTIFICATE_ID).accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()))
            .andExpect(jsonPath("$.name", is(NAME)))
            .andExpect(jsonPath("$.issueDate", is(ISSUE_DATE_TEXT)))
            .andExpect(jsonPath("$.expirationDate", is(EXPIRATION_DATE_TEXT)))
            .andExpect(jsonPath("$.links[0].rel", is("self")))
            .andExpect(jsonPath("$.links[0].href", is(String.format("http://localhost/v1/certificates/%s", CERTIFICATE_ID))))
            .andReturn();
    }

    @Test
    public void whenFindAllByResumeId_thenReturn200() throws Exception {
        given(resumeService.findAllByResumeId(RESUME_ID, Certificate.class)).willReturn(Arrays.asList(certificate));
        mvc.perform(get("/v1/resumes/{resumeId}/certificates", RESUME_ID).accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()))
            .andExpect(jsonPath("$.content[0].name", is(NAME)))
            .andExpect(jsonPath("$.content[0].issueDate", is(ISSUE_DATE_TEXT)))
            .andExpect(jsonPath("$.content[0].expirationDate", is(EXPIRATION_DATE_TEXT)))
            .andExpect(jsonPath("$.content[0].expirationDate", is(EXPIRATION_DATE_TEXT)))
            .andExpect(jsonPath("$.links[0].rel", is("self")))
            .andExpect(jsonPath("$.links[0].href", is(String.format("http://localhost/v1/resumes/%s/certificates", RESUME_ID))))
            .andReturn();
    }

    @Test
    public void whenCreate_thenReturn201() throws Exception {
        given(resumeService.newCertificate(eq(RESUME_ID), any(Certificate.class))).willReturn(certificate);

        mvc.perform(post("/v1/resumes/{resumeId}/certificates", RESUME_ID)
            .content(objectMapper.writeValueAsString(certificateDto))
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()))
            .andReturn();
    }

    @Test
    public void whenSave_thenReturn202() throws Exception {
        doNothing().when(resumeService).save(certificate, Certificate.class);

        mvc.perform(put("/v1/certificates/{id}", CERTIFICATE_ID)
            .content(objectMapper.writeValueAsString(persistedCertificateDto))
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNoContent())
            .andReturn();
    }

    @Test
    public void whenDeleteById_thenReturn202() throws Exception {
        doNothing().when(resumeService).deleteById(CERTIFICATE_ID, Certificate.class);

        mvc.perform(delete("/v1/certificates/{id}", CERTIFICATE_ID))
            .andDo(print())
            .andExpect(status().isNoContent())
            .andReturn();
    }

    @Test
    public void whenDeleteAllByResumeId_thenReturn202() throws Exception {
        doNothing().when(resumeService).deleteAllByResumeId(RESUME_ID, Certificate.class);

        mvc.perform(delete("/v1/resumes/{resumeId}/certificates", RESUME_ID))
            .andDo(print())
            .andExpect(status().isNoContent())
            .andReturn();
    }
}
