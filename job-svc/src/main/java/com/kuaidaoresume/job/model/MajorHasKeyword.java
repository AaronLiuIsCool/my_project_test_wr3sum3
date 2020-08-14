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
@Table(name = "Major_has_Keyword")
public class MajorHasKeyword implements Serializable {
    @Id
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "Major_id")
    private Major major;

    @Id
    @ManyToOne (fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "Keyword_id")
    private Keyword keyword;

    @Column(name = "rating")
    private double rating;
}
