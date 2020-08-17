package com.kuaidaoresume.matching.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompensationDto {

    @NotBlank
    private String currency;
    @NotBlank
    private String type;
    @NotBlank
    private String lowBound;
    @NotBlank
    private String highBound;
}
