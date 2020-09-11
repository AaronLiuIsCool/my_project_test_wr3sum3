package com.kuaidaoresume.job.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "suggestion",
        indexes = {@Index(name = "industry_position_idx", columnList = "industry,position_title")})
public class Suggestion {

    @Id
    private long id;

    @NotNull
    @Size(min = 2, max = 75)
    @Column(name = "industry", length = 75)
    private String industry;

    @NotNull
    @Size(min = 2, max = 75)
    @Column(name = "position_title", length = 75)
    private String positionTitle;

    @NotNull
    private String texts;

    private String suggestionKeywords;
}
