package com.kuaidaoresume.resume.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "basic_info")
public class BasicInfo implements ResumeContainable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length = 256)
    private String fullName;

    @Column(length = 256)
    private String alias;

    @NotNull
    @Column(length = 256)
    private String country;
    private String province;

    @NotNull
    @Column(length = 256)
    private String city;

    @NotNull
    @Email
    @Column(length = 256)
    private String email;

    @NotNull
    @Column(length = 50)
    private String phoneNumber;

    @OneToMany(mappedBy = "basicInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Profile> profiles;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resume_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Resume resume;
}
