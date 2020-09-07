package com.kuaidaoresume.account.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Account {
    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    private String id;
    private String openid;
    private String name;
    private String email;
    private boolean confirmedAndActive;
    private Instant memberSince;
    private boolean support;
    //private String phoneNumber; not for phase I TODO:Woody
    private String photoUrl;
    private String loginType;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "account")
    private List<Resume> resumes; // set tp EAGER or TODO OpenSessionInViewFilter filter
}
