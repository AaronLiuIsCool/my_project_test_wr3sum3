package com.kuaidaoresume.resume.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

import static com.kuaidaoresume.resume.model.Profile.ProfileType;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ProfileDto {
    @NotNull
    private ProfileType type;
    @NotNull
    private String url;
}
