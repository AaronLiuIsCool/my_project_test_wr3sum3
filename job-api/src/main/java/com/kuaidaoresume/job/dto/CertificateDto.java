package com.kuaidaoresume.job.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CertificateDto {

    @NotNull
    private String name;
}
