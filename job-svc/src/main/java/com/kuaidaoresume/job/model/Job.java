package com.kuaidaoresume.job.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;
import com.fasterxml.uuid.Generators;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="job", uniqueConstraints = @UniqueConstraint(columnNames = {"post_date", "position_title", "company_name", "url"}))
/**
 * plan to use mysql events for time to live
 * http://mablomy.blogspot.com/2019/03/ttl-time-to-live-in-mysql.html
 */
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "uuid", nullable = false, updatable = false, unique = true)
    @Builder.Default
    private String uuid = Generators.timeBasedGenerator().generate().toString();
    @Column(name = "post_date", nullable = false, updatable = false, unique = true)
    private Date postDate;
    @Column(name = "position_title", nullable = false, updatable = false, unique = true)
    private String positionTitle;
    @Column(name = "company_name", nullable = false, updatable = false, unique = true)
    private String companyName;
    @Column(name = "url", nullable = false, updatable = true, unique = true)
    private String url;
    @Column(name = "job_type", nullable = true, updatable = true, unique = true)
    private String jobType;
    @Column(name = "employement_type", nullable = true, updatable = true, unique = true)
    private String employementType;
    @Column(name = "agency", nullable = true, updatable = true, unique = true)
    private String agency;
    @Column(name = "salary_min", nullable = true, updatable = false)
    private int salaryMin;
    @Column(name = "salary_max", nullable = true, updatable = false)
    private int salaryMax;
    @Column(name = "salary_currency", nullable = true, updatable = false)
    private String currency;
    @Column(name = "year_experience_required", nullable = true, updatable = false)
    private int yearExperienceRequired;
    @Column(name = "certificate_required", nullable = true, updatable = false)
    private String certificateRequired;
    @Column(name = "education_required", nullable = true, updatable = false)
    private String educationRequired;
    @Column(name = "job_post_id", nullable = true, updatable = false)
    private String jobPostIdentifier;
    @Column(name = "job_description", nullable = true, updatable = false)
    private String jobDescription;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY) //keep location when deleting job
    @JoinColumn(name = "location_id", referencedColumnName="id")
    private Location location;
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(
            name = "job_has_required_major",
            joinColumns = { @JoinColumn(name = "job_id", referencedColumnName="id") },
            inverseJoinColumns = { @JoinColumn(name = "major_id", referencedColumnName="id") }
    )
    private List<Major> majors;
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<JobHasKeyword> jobHasKeywords = new HashSet<>();
}
