package com.kuaidaoresume.resume.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Education implements ResumeContainable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 60)
    @Column(length = 60)
    private String country;

    @NotNull
    @Size(min = 2, max = 100)
    @Column(length = 100)
    private String city;

    @NotNull
    @Size(min = 2, max = 50)
    @Column(length = 50)
    private String institution;

    @NotNull
    @Size(min = 2, max = 50)
    @Column(length = 50)
    private String major;

    @NotNull
    @Size(min = 2, max = 20)
    @Column(length = 20)
    private String degree;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    @Size(min = 1, max = 4)
    @Column(length = 4)
    private String gpa;

    @OneToMany(mappedBy = "education", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Award> awards;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;
}
