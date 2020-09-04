package com.kuaidaoresume.matching.model;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Compensation {

    @NotNull
    private String currency;

    @NotNull
    private Type type;

    @NotNull
    private String lowBound;

    @NotNull
    private String highBound;

    @Getter
    public enum Type {
        SALARY("salary"), WAGE("wage");

        private final String value;

        Type(String value) {
            this.value = value;
        }
    }
}
