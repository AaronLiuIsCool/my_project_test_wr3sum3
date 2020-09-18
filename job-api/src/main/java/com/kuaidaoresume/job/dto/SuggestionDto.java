package com.kuaidaoresume.job.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuggestionDto {

    @NotNull
    private long id;

    @NotNull
    private String industry;

    @NotNull
    private String positionTitle;

    @NotNull
    private String texts;

    private String suggestionKeywords;
}
