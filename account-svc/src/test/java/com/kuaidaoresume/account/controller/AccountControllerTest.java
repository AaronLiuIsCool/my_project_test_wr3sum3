package com.kuaidaoresume.account.controller;

import com.kuaidaoresume.account.TestConfig;
import com.kuaidaoresume.account.client.AccountClient;
import com.kuaidaoresume.account.dto.*;
import com.kuaidaoresume.account.model.Account;
import com.kuaidaoresume.common.api.BaseResponse;
import com.kuaidaoresume.common.api.ResultCode;
import com.kuaidaoresume.common.auth.AuthConstant;
import com.kuaidaoresume.common.env.EnvConfig;
import com.kuaidaoresume.mail.client.MailClient;
import com.kuaidaoresume.mail.dto.EmailRequest;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@EnableFeignClients(basePackages = {"com.kuaidaoresume.account.client"})
@EnableAutoConfiguration
@Import(TestConfig.class)
@Slf4j
@ActiveProfiles("it")
public class AccountControllerTest {

    @Autowired
    AccountClient accountClient;

    @Autowired
    EnvConfig envConfig;

    @MockBean
    MailClient mailClient;

    private Account newAccount;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testChangeEmail() {
        // arrange mock
        when(mailClient.send(any(EmailRequest.class)))
                .thenReturn(BaseResponse.builder().message("email sent").build());

        String name = "testAccount";
        String email = "test@kuaidaoresume.com";
        CreateAccountRequest createAccountRequest = CreateAccountRequest.builder()
                .name(name)
                .email(email)
                .build();
        // create one account
        GenericAccountResponse genericAccountResponse = accountClient
                .createAccount(AuthConstant.AUTHORIZATION_WWW_SERVICE, createAccountRequest);
        assertThat(genericAccountResponse.isSuccess()).isTrue();
        AccountDto accountDto = genericAccountResponse.getAccount();
        assertThat(accountDto.isConfirmedAndActive()).isFalse();

        // change email
        String changedEmail = "test123@kuaidaoresume.com";
        EmailConfirmation emailConfirmation = EmailConfirmation.builder()
                .userId(accountDto.getId())
                .email(changedEmail)
                .build();
        BaseResponse baseResponse = accountClient.changeEmail(AuthConstant.AUTHORIZATION_WWW_SERVICE, emailConfirmation);
        log.info(baseResponse.toString());
        assertThat(baseResponse.isSuccess()).isTrue();

        // verify email changed and account activated
        GetOrCreateRequest getOrCreateRequest = GetOrCreateRequest.builder()
                .email(changedEmail)
                .build();
        genericAccountResponse = accountClient.getOrCreateAccount(AuthConstant.AUTHORIZATION_WWW_SERVICE, getOrCreateRequest);
        AccountDto foundAccountDto = genericAccountResponse.getAccount();
        assertThat(foundAccountDto.getEmail()).isEqualTo(changedEmail);
        assertThat(foundAccountDto.isConfirmedAndActive()).isTrue();

        // account not found
        exceptionRule.expect(FeignException.class);
        emailConfirmation = EmailConfirmation.builder()
                .userId("not_existing_id")
                .email(changedEmail)
                .build();
        baseResponse = accountClient.changeEmail(AuthConstant.AUTHORIZATION_WWW_SERVICE, emailConfirmation);
        log.info(baseResponse.toString());
        assertThat(baseResponse.isSuccess()).isFalse();
        assertThat(baseResponse.getCode()).isEqualTo(ResultCode.NOT_FOUND);
    }
}
