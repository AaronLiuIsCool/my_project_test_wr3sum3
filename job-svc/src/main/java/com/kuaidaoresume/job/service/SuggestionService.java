package com.kuaidaoresume.job.service;

import com.kuaidaoresume.job.dto.SuggestionDto;
import com.kuaidaoresume.job.dto.SuggestionListDto;
import com.kuaidaoresume.job.model.Suggestion;

import java.util.List;

public interface SuggestionService {

    SuggestionDto getSuggestionById(Long id);

    SuggestionListDto getAListByIndustryAndPosition(String industry, String position, int offset, int limit);

    SuggestionDto addSuggestion(String industry, String position, String texts, String keywords);

    SuggestionDto updateSuggestion(SuggestionDto updateSuggestion);

    void deleteASuggestion(Long id);
}
