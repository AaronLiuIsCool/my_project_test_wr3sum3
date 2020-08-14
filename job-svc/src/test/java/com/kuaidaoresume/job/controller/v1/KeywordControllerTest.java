package com.kuaidaoresume.job.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuaidaoresume.common.auth.AuthConstant;
import com.kuaidaoresume.job.config.JobApplicationTestConfig;
import com.kuaidaoresume.job.controller.assembler.KeywordRepresentationModelAssembler;
import com.kuaidaoresume.job.service.KeywordService;
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
import java.util.Optional;

@Import({ KeywordRepresentationModelAssembler.class, JobApplicationTestConfig.class })
@ExtendWith(SpringExtension.class)
@WebMvcTest(KeywordController.class)
public class KeywordControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private KeywordService keywordService;

    @Test
    public void whenFindById_thenReturn200() throws Exception {
        Keyword keyword = Keyword.builder()
                .id(1L)
                .name("aKeyword")
                .build();
        when(keywordService.findKeywordById(1L)).thenReturn(Optional.of(modelMapper.map(keyword, KeywordDto.class)));

        mvc.perform(get("/v1/keywords/{id}", 1L).header(HttpHeaders.AUTHORIZATION, AuthConstant.AUTHORIZATION_SUPPORT_USER).accept(MediaTypes.HAL_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$.name", is("aKeyword")))
                .andReturn();
        verify(keywordService, times(1)).findKeywordById(1L);
        verifyNoMoreInteractions(keywordService);
    }

    @Test
    public void whenFindAllKeywords_thenReturn200() throws Exception {
        Keyword first = Keyword.builder()
                .id(1L)
                .name("keyword1")
                .build();

        Keyword second = Keyword.builder()
                .id(2L)
                .name("keyword2")
                .build();

        when(keywordService.findAll()).thenReturn(Arrays.asList(modelMapper.map(first, KeywordDto.class), modelMapper.map(second, KeywordDto.class)));

        mvc.perform(get("/v1/keywords/all").header(HttpHeaders.AUTHORIZATION, AuthConstant.AUTHORIZATION_SUPPORT_USER).accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andDo(print())
                .andExpect(jsonPath("$.content[0].name", is("keyword1")))
                .andExpect(jsonPath("$.content[1].name", is("keyword2")));

        verify(keywordService, times(1)).findAll();
        verifyNoMoreInteractions(keywordService);
    }

    @Test
    public void whenSaveKeyword_thenReturn201() throws Exception {
        Keyword keyword = Keyword.builder()
                .id(1L)
                .name("aKeyword")
                .build();

        when(keywordService.createKeyword(any(KeywordDto.class))).thenReturn(Optional.of(modelMapper.map(keyword, KeywordDto.class)));

        mvc.perform(post("/v1/keywords").header(HttpHeaders.AUTHORIZATION, AuthConstant.AUTHORIZATION_SUPPORT_USER)
                .content(objectMapper.writeValueAsString(modelMapper.map(keyword, KeywordDto.class)))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        verify(keywordService, times(1)).createKeyword(modelMapper.map(keyword, KeywordDto.class));
        verifyNoMoreInteractions(keywordService);
    }

    @Test
    public void whenDeleteById_thenReturn202() throws Exception {
        doNothing().when(keywordService).deleteKeywordById(any(Long.class));

        mvc.perform(delete("/v1/keywords/id/{id}", 1L).header(HttpHeaders.AUTHORIZATION, AuthConstant.AUTHORIZATION_SUPPORT_USER))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();

        verify(keywordService, times(1)).deleteKeywordById(1L);
        verifyNoMoreInteractions(keywordService);
    }

    @Test
    public void whenDeleteByName_thenReturn202() throws Exception {
        doNothing().when(keywordService).deleteKeywordByName(any(String.class));

        mvc.perform(delete("/v1/keywords/name/{name}", "keyword1").header(HttpHeaders.AUTHORIZATION, AuthConstant.AUTHORIZATION_SUPPORT_USER))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
        verify(keywordService, times(1)).deleteKeywordByName("keyword1");
        verifyNoMoreInteractions(keywordService);
    }
}