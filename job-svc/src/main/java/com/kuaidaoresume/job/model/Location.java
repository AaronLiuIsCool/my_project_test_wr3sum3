package com.kuaidaoresume.job.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "location",
        uniqueConstraints=@UniqueConstraint(columnNames={"country", "city","post_code"}))
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(name = "country", nullable = false, updatable = false)
    private String country;
    @Column(name = "city", nullable = false, updatable = false)
    private String city;
    @Column(name = "state", nullable = true, updatable = false) //bug fix: state is already in db, alter table needed
    private String state;
    @Column(name = "post_code", nullable = true, updatable = false)
    private String postCode;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<LocationHasKeyword> locationHasKeywords = new HashSet<>();

//    @OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY, orphanRemoval = true)
//    @JoinColumn(name = "Job_id", referencedColumnName="id")
//    private Set<Job> jobs;
}
