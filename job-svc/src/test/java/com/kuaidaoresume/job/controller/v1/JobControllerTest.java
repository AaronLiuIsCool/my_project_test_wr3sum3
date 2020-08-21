package com.kuaidaoresume.job.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuaidaoresume.common.auth.AuthConstant;
import com.kuaidaoresume.job.controller.assembler.JobRepresentationModelAssembler;
import com.kuaidaoresume.job.config.JobApplicationTestConfig;
import com.kuaidaoresume.job.service.JobInfoExtractionService;
import com.kuaidaoresume.job.service.JobService;
import com.kuaidaoresume.job.dto.*;
import com.kuaidaoresume.job.model.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
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

@Import({ JobRepresentationModelAssembler.class, JobApplicationTestConfig.class })
@ExtendWith(SpringExtension.class)
@WebMvcTest(JobController.class)
public class JobControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private JobService jobService;

    @MockBean
    private JobInfoExtractionService jobInfoExtractionService;

    @Test
    public void whenFindById_thenReturn200() throws Exception {
        Job job = Job.builder()
                .id(1L)
                .postDate(new Date(System.currentTimeMillis()))
                .positionTitle("SDE")
                .companyName("ABC")
                .build();
        when(jobService.findJobById(1L)).thenReturn(Optional.of(modelMapper.map(job, JobDto.class)));

        mvc.perform(get("/v1/jobs/{id}", 1L).header(HttpHeaders.AUTHORIZATION, AuthConstant.AUTHORIZATION_SUPPORT_USER).accept(MediaTypes.HAL_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$.positionTitle", is("SDE")))
                .andExpect(jsonPath("$.companyName", is("ABC")))
                .andReturn();
        verify(jobService, times(1)).findJobById(1L);
        verifyNoMoreInteractions(jobService);
    }

    @Test
    public void whenFindAllJobs_thenReturn200() throws Exception {
        Job first = Job.builder()
                .id(1L)
                .postDate(new Date(System.currentTimeMillis()))
                .positionTitle("positionTitle1")
                .companyName("companyName1")
                .build();

        Job second = Job.builder()
                .id(2L)
                .postDate(new Date(System.currentTimeMillis()))
                .positionTitle("positionTitle2")
                .companyName("companyName2")
                .build();

        when(jobService.findAll()).thenReturn(Arrays.asList(modelMapper.map(first, JobDto.class), modelMapper.map(second, JobDto.class)));

        mvc.perform(get("/v1/jobs/all").header(HttpHeaders.AUTHORIZATION, AuthConstant.AUTHORIZATION_SUPPORT_USER).accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andDo(print())
                .andExpect(jsonPath("$.content[0].positionTitle", is("positionTitle1")))
                .andExpect(jsonPath("$.content[1].positionTitle", is("positionTitle2")))
                .andExpect(jsonPath("$.content[0].companyName", is("companyName1")))
                .andExpect(jsonPath("$.content[1].companyName", is("companyName2")));

        verify(jobService, times(1)).findAll();
        verifyNoMoreInteractions(jobService);
    }

    @Test
    public void whenCreateJob_thenReturn201() throws Exception {
        Job job = Job.builder()
                .id(1L)
                .postDate(new Date(System.currentTimeMillis()))
                .positionTitle("SDE")
                .companyName("ABC")
                .url("someUrl")
                .build();

        when(jobService.createJob(any(JobDto.class))).thenReturn(Optional.of(modelMapper.map(job, JobDto.class)));

        mvc.perform(post("/v1/jobs").header(HttpHeaders.AUTHORIZATION, AuthConstant.AUTHORIZATION_SUPPORT_USER)
                .content(objectMapper.writeValueAsString(modelMapper.map(job, JobDto.class)))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        verify(jobService, times(1)).createJob(modelMapper.map(job, JobDto.class));
        verifyNoMoreInteractions(jobService);
    }

    @Test
    public void whenUpdateJob_thenReturn200() throws Exception {
        Job job = Job.builder()
                .id(1L)
                .postDate(new Date(System.currentTimeMillis()))
                .positionTitle("SDE")
                .companyName("ABC")
                .url("someUrl")
                .build();

        when(jobService.updateJob(any(JobDto.class))).thenReturn(Optional.of(modelMapper.map(job, JobDto.class)));

        mvc.perform(put("/v1/jobs").header(HttpHeaders.AUTHORIZATION, AuthConstant.AUTHORIZATION_SUPPORT_USER)
                .content(objectMapper.writeValueAsString(modelMapper.map(job, JobDto.class)))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();

        verify(jobService, times(1)).updateJob(modelMapper.map(job, JobDto.class));
        verifyNoMoreInteractions(jobService);
    }

    @Test
    public void whenDeleteById_thenReturn202() throws Exception {
        doNothing().when(jobService).deleteJobById(any(Long.class));

        mvc.perform(delete("/v1/jobs/{id}", 1L).header(HttpHeaders.AUTHORIZATION, AuthConstant.AUTHORIZATION_SUPPORT_USER))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andReturn();
        verify(jobService, times(1)).deleteJobById(1L);
        verifyNoMoreInteractions(jobService);
    }
}