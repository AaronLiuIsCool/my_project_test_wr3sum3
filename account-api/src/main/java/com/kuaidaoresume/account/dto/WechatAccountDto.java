package com.kuaidaoresume.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WechatAccountDto {

    @NotBlank
    private String openid;
    @NotBlank
    private String nickname;
    private int sex;
    private String province;
    private String city;
    private String country;
    @NotEmpty
    private String headimgurl;
    @NotEmpty
    private String[] privilege;
    private String unionid;

    private String email;

}
