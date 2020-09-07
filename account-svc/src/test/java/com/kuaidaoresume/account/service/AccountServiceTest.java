package com.kuaidaoresume.account.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.kuaidaoresume.account.AccountConstant;
import com.kuaidaoresume.account.dto.AccountDto;
import com.kuaidaoresume.account.model.Account;
import com.kuaidaoresume.account.props.AppProps;
import com.kuaidaoresume.account.repo.AccountRepo;
import com.kuaidaoresume.account.repo.AccountSecretRepo;
import com.kuaidaoresume.account.service.helper.ServiceHelper;
import com.kuaidaoresume.common.api.BaseResponse;
import com.kuaidaoresume.common.env.EnvConfig;
import com.kuaidaoresume.mail.client.MailClient;
import com.kuaidaoresume.mail.dto.EmailRequest;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    @Mock
    private AccountRepo accountRepo;

    @Mock
    private AccountSecretRepo accountSecretRepo;

    @Mock
    private AppProps appProps;

    @Mock
    private EnvConfig envConfig;

    @Mock
    private MailClient mailClient;

    @Mock
    private ServiceHelper serviceHelper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AccountService accountService;

    // TODO Aaron Liu @Test testSendEmail

    @Test
    public void testModelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        Account account = Account.builder().id("123456")
                .name("testAccount")
                .email("test@kuaidaoresume.net")
                .memberSince(Instant.now())
                .confirmedAndActive(true)
                .photoUrl("https://kuaidaoresume.com/photo/test.png")
                //.phoneNumber("6041234567")
                .support(false)
                .build();

        AccountDto accountDto = modelMapper.map(account, AccountDto.class);
        validateAccount(accountDto, account);

        Account account1 = modelMapper.map(accountDto, Account.class);
        validateAccount(accountDto, account1);
    }

    void validateAccount(AccountDto accountDto, Account account) {
        assertThat(account.getId()).isEqualTo(accountDto.getId());
        assertThat(account.getName()).isEqualTo(accountDto.getName());
        assertThat(account.getEmail()).isEqualTo(accountDto.getEmail());
        assertThat(account.getMemberSince()).isEqualTo(accountDto.getMemberSince());
        assertThat(account.isConfirmedAndActive()).isEqualTo(accountDto.isConfirmedAndActive());
        assertThat(account.getPhotoUrl()).isEqualTo(accountDto.getPhotoUrl());
        //assertThat(account.getPhoneNumber()).isEqualTo(accountDto.getPhoneNumber());
        assertThat(account.isSupport()).isEqualTo(accountDto.isSupport());
    }

}
