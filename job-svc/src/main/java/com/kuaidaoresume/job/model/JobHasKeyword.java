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
import java.util.List;

@Table(name = "Job_has_Keyword")
public class JobHasKeyword {

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("Job_id")
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("Keyword_id")
    private Keyword keyword;

    @Column(name = "rating")
    private double rating;

}