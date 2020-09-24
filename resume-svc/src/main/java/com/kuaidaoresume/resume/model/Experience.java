package com.kuaidaoresume.resume.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;

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
    @Size(min = 2, max = 50)
    @Column(length = 50)
    protected String role;

    @NotNull
    @Size(min = 2, max = 50)
    @Column(length = 50)
    private String organization;

    @NotNull
    @Size(min = 2, max = 100)
    @Column(length = 100)
    private String city;

    @NotNull
    @Size(min = 2, max = 60)
    @Column(length = 60)
    private String country;

    @NotNull
    private Date startDate;

    private Date endDate;

    @NotNull
    @Size(min = 3, max = 2000)
    @Column(length = 2000)
    private String description;

    public abstract Resume getResume();

    public abstract void setResume(Resume resume);
}
