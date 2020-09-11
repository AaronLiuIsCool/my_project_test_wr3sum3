package com.kuaidaoresume.job.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SuggestionCreateRequest {

    private String industry;

    private String positionTitle;

    private String texts;

    private String suggestionKeywords;
}
