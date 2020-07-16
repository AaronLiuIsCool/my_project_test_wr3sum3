package com.kuaidaoresume.resume.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BasicInfoDto {
    @NotNull
    private String fullName;
    private String alias;
    private String country;
    private String province;
    @NotNull
    private String city;
    @NotNull
    @Email
    private String email;
    @NotNull
    private String phoneNumber;
    private List<ProfileDto> profiles;
    private String resumeId;
}
