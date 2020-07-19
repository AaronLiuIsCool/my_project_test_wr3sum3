package com.kuaidaoresume.resume.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String country;
    @NotNull
    private String city;
    @NotNull
    private String institution;
    @NotNull
    private String major;
    @NotNull
    private String degree;
    @NotNull
    private Date startDate;
    @NotNull
    private Date endDate;
    private String gpa;

    @OneToMany(mappedBy = "education", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Award> awards;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;
}
