package com.kuaidaoresume.job.model;

import lombok.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Set;
import java.util.HashSet;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="keyword", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class Keyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    @OneToMany(mappedBy = "keyword", cascade = CascadeType.DETACH, orphanRemoval = true)
    @Builder.Default
    private Set<JobHasKeyword> jobHasKeywords = new HashSet<>();

    @OneToMany(mappedBy = "keyword", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<LocationHasKeyword> locationHasKeywords = new HashSet<>();

    @OneToMany(mappedBy = "keyword", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<MajorHasKeyword> majorHasKeywords = new HashSet<>();
}
