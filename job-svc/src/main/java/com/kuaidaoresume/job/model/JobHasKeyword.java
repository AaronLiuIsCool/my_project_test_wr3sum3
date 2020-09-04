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
@Table(name = "job_has_keyword")
public class JobHasKeyword implements Serializable{
    @Id
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "job_id")
    private Job job;

    @Id
    @ManyToOne (fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "keyword_id")
    private Keyword keyword;

    @Column(name = "rating")
    private double rating;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long jobId = job.getId();
        long keywordId = keyword.getId();
        result = prime * result + (int) (jobId^(jobId >>> 32));
        result = prime * result + (int) (keywordId^(keywordId >>> 32));
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        JobHasKeyword other = (JobHasKeyword) obj;
        long jobId = job.getId();
        long keywordId = keyword.getId();
        if (jobId != other.job.getId()) {
            return false;
        }
        if (keywordId != other.keyword.getId()) {
            return false;
        }
        return true;
    }

}