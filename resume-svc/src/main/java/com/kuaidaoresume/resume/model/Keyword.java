package com.kuaidaoresume.resume.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Keyword {

    @Id
    @Size(min = 1, max = 100)
    @Column(length = 100, nullable = false, updatable = false, unique = true)
    private String value;

    @NotNull
    @Column(length = 2, nullable = false, updatable = false)
    private String language;
}
