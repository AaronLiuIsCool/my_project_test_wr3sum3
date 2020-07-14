package com.kuaidaoresume.resume.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class BasicInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    private String fullName;
    private String alias;
    private String country;
    private String province;
    @NotNull
    private String city;
    @NotNull
    @Email
    private String email;
    @NotNull
    private String phoneNumber;
    @OneToMany(mappedBy = "basic_info", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Profile> profiles;
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;
}
