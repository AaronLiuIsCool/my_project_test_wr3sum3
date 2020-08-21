package com.kuaidaoresume.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
// import com.kuaidaoresume.common.validation.PhoneNumber; not for phase I TODO:Woody

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetOrCreateRequest {
    private String name;
    @Email(message = "Invalid email")
    private String email;
    //@PhoneNumber
    //private String phoneNumber; not for phase I TODO:Woody

    @AssertTrue(message = "Empty request")
    private boolean isValidRequest() {
        //return StringUtils.hasText(name) || StringUtils.hasText(email) || StringUtils.hasText(phoneNumber); not for phase I TODO:Woody
        return StringUtils.hasText(name) || StringUtils.hasText(email);
    }
}
