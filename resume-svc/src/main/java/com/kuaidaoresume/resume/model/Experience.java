package com.kuaidaoresume.resume.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@MappedSuperclass
public abstract class Experience implements ResumeContainable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @NotNull
    @Column(length = 500)
    protected String role;

    @NotNull
    @Column(length = 500)
    private String organization;

    @NotNull
    @Column(length = 256)
    private String city;

    @NotNull
    @Column(length = 256)
    private String country;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull
    @Column(length = 5000)
    private String description;

    public abstract Resume getResume();

    public abstract void setResume(Resume resume);
}
