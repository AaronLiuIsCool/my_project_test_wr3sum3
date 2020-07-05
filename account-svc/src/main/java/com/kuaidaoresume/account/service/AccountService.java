package com.kuaidaoresume.account.service;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.kuaidaoresume.account.AccountConstant;
import com.kuaidaoresume.account.dto.AccountDto;
import com.kuaidaoresume.account.model.Account;
import com.kuaidaoresume.account.model.AccountSecret;
import com.kuaidaoresume.account.props.AppProps;
import com.kuaidaoresume.account.dto.AccountList;
import com.kuaidaoresume.account.repo.AccountRepo;
import com.kuaidaoresume.account.repo.AccountSecretRepo;
//import com.kuaidaoresume.account.service.helper.ServiceHelper;
import com.kuaidaoresume.common.api.BaseResponse;
import com.kuaidaoresume.common.api.ResultCode;
import com.kuaidaoresume.common.auditlog.LogEntry;
import com.kuaidaoresume.common.auth.AuthConstant;
import com.kuaidaoresume.common.auth.AuthContext;
//import com.kuaidaoresume.common.crypto.Sign;
import com.kuaidaoresume.common.env.EnvConfig;
import com.kuaidaoresume.common.error.ServiceException;
//import com.kuaidaoresume.common.utils.Helper;
//import com.kuaidaoresume.mail.client.MailClient;
//import com.kuaidaoresume.mail.dto.EmailRequest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Account services impls
 * @author Aaron Liu
 */
@Service
@RequiredArgsConstructor
public class AccountService {

    static ILogger logger = SLoggerFactory.getLogger(AccountService.class);

    private final AccountRepo accountRepo;

    private final AccountSecretRepo accountSecretRepo;

    private final AppProps appProps;

    private final EnvConfig envConfig;

//    private final MailClient mailClient;

//    private final ServiceHelper serviceHelper;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;

    @PersistenceContext
    private EntityManager entityManager;

    public AccountDto get(String userId) {
        Account account = accountRepo.findAccountById(userId);
        if (account == null) {
            throw new ServiceException(String.format("User with id %s not found", userId));
        }
        return this.convertToDto(account);
    }

    private AccountDto convertToDto(Account account) {
        return modelMapper.map(account, AccountDto.class);
    }

    private Account convertToModel(AccountDto accountDto) {
        return modelMapper.map(accountDto, Account.class);
    }

}
