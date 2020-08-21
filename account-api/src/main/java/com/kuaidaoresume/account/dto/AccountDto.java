package com.kuaidaoresume.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Instant;

/**
 * Account DTO
 *
 * @author Aaron Liu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDto {
    @NotBlank
    private String id;
    private String name;
    @Email(message = "Invalid email")
    private String email;
    private boolean confirmedAndActive;
    @NotNull
    private Instant memberSince;
    private boolean support;

    //private String phoneNumber; not for phase I TODO:Woody
    @NotEmpty
    private String photoUrl;
}