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
    @Column(length = 256)
    private String country;

    @NotNull
    @Column(length = 256)
    private String city;

    @NotNull
    @Column(length = 500)
    private String institution;

    @NotNull
    @Column(length = 256)
    private String major;

    @NotNull
    @Column(length = 256)
    private String degree;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    @Column(length = 10)
    private String gpa;

    @OneToMany(mappedBy = "education", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Award> awards;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;
}
