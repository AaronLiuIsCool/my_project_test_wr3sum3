package com.kuaidaoresume.matching.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Compensation {

    @NotNull
    private String currency;

    @NotNull
    private String type;

    @NotNull
    private String lowBound;

    @NotNull
    private String highBound;
}
