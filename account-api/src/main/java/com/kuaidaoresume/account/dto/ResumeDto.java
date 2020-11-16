package com.kuaidaoresume.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.Instant;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResumeDto {
    @NotBlank
    private String resumeId;
    private String alias;
    private String thumbnailUri;
    private Instant createdAt;
}
