package com.kuaidaoresume.job.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuaidaoresume.common.auth.AuthConstant;
import com.kuaidaoresume.job.config.JobApplicationTestConfig;
import com.kuaidaoresume.job.dto.*;
import com.kuaidaoresume.job.model.*;

import com.kuaidaoresume.job.service.SuggestionService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.modelmapper.ModelMapper;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Import({ JobApplicationTestConfig.class })
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = SuggestionController.class)
public class SuggestionControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private SuggestionService suggestionService;

    @Test
    public void whenCall_getSuggestionById_return200() throws Exception {
        Suggestion suggestion = Suggestion.builder()
            .id(1L)
            .industry("test_industry")
            .positionTitle("test_pTitile")
            .texts("test_texts")
            .suggestionKeywords(null)
            .build();

        when(suggestionService.getSuggestionById(1L))
            .thenReturn(modelMapper.map(suggestion, SuggestionDto.class));

        mvc.perform(get("/v1/suggestion/{id}", 1L)
            .header(HttpHeaders.AUTHORIZATION, AuthConstant.AUTHORIZATION_SUPPORT_USER)
            .accept(MediaTypes.HAL_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
            .andExpect(jsonPath("$.suggestion.industry", is("test_industry")))
            .andExpect(jsonPath("$.suggestion.positionTitle", is("test_pTitile")))
            .andReturn();

        verify(suggestionService, times(1)).getSuggestionById(1L);
        verifyNoMoreInteractions(suggestionService);
    }

    @Test
    public void whenCall_getAListByIndustryAndPosition_return200() throws Exception {
        Suggestion suggestion1 = Suggestion.builder()
            .id(1L)
            .industry("test_industry")
            .positionTitle("test_pTitile")
            .texts("test_texts")
            .suggestionKeywords("keywords1")
            .build();

        Suggestion suggestion2 = Suggestion.builder()
            .id(2L)
            .industry("test_industry")
            .positionTitle("test_pTitile")
            .texts("test_texts2")
            .suggestionKeywords("keywords2,keywords3")
            .build();

        SuggestionListDto suggestionListDto = new SuggestionListDto();
        suggestionListDto.setSuggestions(Arrays.asList(modelMapper.map(suggestion1, SuggestionDto.class),
                modelMapper.map(suggestion2, SuggestionDto.class)));
        when(suggestionService.getAListByIndustryAndPosition("test_industry", "test_pTitile", 0, 2))
            .thenReturn(suggestionListDto);

        mvc.perform(get("/v1/get_suggestions_by_industry_position")
            .header(HttpHeaders.AUTHORIZATION, AuthConstant.AUTHORIZATION_WWW_SERVICE)
            .accept(MediaTypes.HAL_JSON_VALUE)
            .param("industry", "test_industry")
            .param("position", "test_pTitile")
            .param("offset", "0")
            .param("limit", "2"))
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
            .andExpect(jsonPath("$.suggestions[0].industry", is("test_industry")))
            .andExpect(jsonPath("$.suggestions[0].positionTitle", is("test_pTitile")))
            .andExpect(jsonPath("$.suggestions[0].texts", is("test_texts")))
            .andExpect(jsonPath("$.suggestions[0].suggestionKeywords", is("keywords1")))
            .andExpect(jsonPath("$.suggestions[1].industry", is("test_industry")))
            .andExpect(jsonPath("$.suggestions[1].positionTitle", is("test_pTitile")))
            .andExpect(jsonPath("$.suggestions[1].texts", is("test_texts2")))
            .andExpect(jsonPath("$.suggestions[1].suggestionKeywords", is("keywords2,keywords3")))
            .andReturn();

        verify(suggestionService, times(1)).getAListByIndustryAndPosition("test_industry", "test_pTitile", 0, 2);
        verifyNoMoreInteractions(suggestionService);
    }

    @Test
    public void whenCall_addSuggestion_return200() throws Exception {
        Suggestion suggestion = Suggestion.builder()
            .id(1L)
            .industry("test_industry")
            .positionTitle("test_pTitile")
            .texts("test_texts")
            .suggestionKeywords("test1,test2,test3")
            .build();
        when(suggestionService.addSuggestion("test_industry", "test_pTitile", "test_texts", "test1,test2,test3"))
            .thenReturn(modelMapper.map(suggestion, SuggestionDto.class));

        mvc.perform(post("/v1/suggestion")
            .header(HttpHeaders.AUTHORIZATION, AuthConstant.AUTHORIZATION_SUPPORT_USER)
            .content(objectMapper.writeValueAsString(modelMapper.map(suggestion, SuggestionDto.class)))
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.suggestion.industry", is("test_industry")))
            .andExpect(jsonPath("$.suggestion.positionTitle", is("test_pTitile")))
            .andExpect(jsonPath("$.suggestion.texts", is("test_texts")))
            .andExpect(jsonPath("$.suggestion.suggestionKeywords", is("test1,test2,test3")))
            .andReturn();

        verify(suggestionService, times(1)).addSuggestion("test_industry", "test_pTitile", "test_texts", "test1,test2,test3");
        verifyNoMoreInteractions(suggestionService);
    }

    @Test
    public void whenCall_updateSuggestion_return200() throws Exception {
        Suggestion suggestion = Suggestion.builder()
            .id(1L)
            .industry("test_industry")
            .positionTitle("test_pTitile")
            .texts("test_texts")
            .suggestionKeywords("test1,test2")
            .build();

        when(suggestionService.updateSuggestion(modelMapper.map(suggestion, SuggestionDto.class)))
            .thenReturn(modelMapper.map(suggestion, SuggestionDto.class));

        mvc.perform(put("/v1/suggestion/{id}", 1L)
            .header(HttpHeaders.AUTHORIZATION, AuthConstant.AUTHORIZATION_SUPPORT_USER)
            .content(objectMapper.writeValueAsString(modelMapper.map(suggestion, SuggestionDto.class)))
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.suggestion.industry", is("test_industry")))
            .andExpect(jsonPath("$.suggestion.positionTitle", is("test_pTitile")))
            .andExpect(jsonPath("$.suggestion.texts", is("test_texts")))
            .andExpect(jsonPath("$.suggestion.suggestionKeywords", is("test1,test2")))
            .andReturn();

        verify(suggestionService, times(1)).updateSuggestion(modelMapper.map(suggestion, SuggestionDto.class));
        verifyNoMoreInteractions(suggestionService);
    }

    @Test
    public void whenCall_deleteASuggestion_return200() throws Exception {
        doNothing().when(suggestionService).deleteASuggestion(1L);

        mvc.perform(delete("/v1/suggestion/{id}", 1L)
            .header(HttpHeaders.AUTHORIZATION, AuthConstant.AUTHORIZATION_SUPPORT_USER))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

        verify(suggestionService, times(1)).deleteASuggestion(1L);
        verifyNoMoreInteractions(suggestionService);
    }
}
