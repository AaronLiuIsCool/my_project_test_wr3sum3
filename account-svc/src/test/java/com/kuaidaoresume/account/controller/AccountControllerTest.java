package com.kuaidaoresume.account.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.ribbon.FeignRibbonClientAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import com.kuaidaoresume.account.TestConfig;
import com.kuaidaoresume.account.client.AccountClient;
import com.kuaidaoresume.account.dto.*;
import com.kuaidaoresume.account.model.Account;
import com.kuaidaoresume.account.repo.AccountRepo;
import com.kuaidaoresume.account.repo.AccountSecretRepo;
import com.kuaidaoresume.common.api.BaseResponse;
import com.kuaidaoresume.common.api.ResultCode;
import com.kuaidaoresume.common.auth.AuthConstant;
import com.kuaidaoresume.common.env.EnvConfig;
import com.kuaidaoresume.mail.client.MailClient;
import com.kuaidaoresume.mail.dto.EmailRequest;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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

    @Autowired
    AccountRepo accountRepo;

    @Autowired
    AccountSecretRepo accountSecretRepo;

    private Account newAccount;

    @Before
    public void setUp() {
        // sanity check
        accountRepo.deleteAll();
        // clear CURRENT_USER_HEADER for testing
        TestConfig.TEST_USER_ID = null;
    }

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
        emailConfirmation = EmailConfirmation.builder()
                .userId("not_existing_id")
                .email(changedEmail)
                .build();
        baseResponse = accountClient.changeEmail(AuthConstant.AUTHORIZATION_WWW_SERVICE, emailConfirmation);
        log.info(baseResponse.toString());
        assertThat(baseResponse.isSuccess()).isFalse();
        assertThat(baseResponse.getCode()).isEqualTo(ResultCode.NOT_FOUND);
    }

    @After
    public void destroy() {
        accountRepo.deleteAll();
    }
}