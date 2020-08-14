package com.kuaidaoresume.job.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Job_has_Keyword")
public class JobHasKeyword implements Serializable{
    @Id
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "Job_id")
    private Job job;

    @Id
    @ManyToOne (fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "Keyword_id")
    private Keyword keyword;

    @Column(name = "rating")
    private double rating;
}