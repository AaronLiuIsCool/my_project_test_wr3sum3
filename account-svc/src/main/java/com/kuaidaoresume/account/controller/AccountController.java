package com.kuaidaoresume.account.controller;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import com.kuaidaoresume.account.service.AccountService;
import com.kuaidaoresume.common.auth.*;
import com.kuaidaoresume.common.env.EnvConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.kuaidaoresume.account.dto.*;
import com.kuaidaoresume.account.service.AccountService;
import com.kuaidaoresume.common.api.BaseResponse;
import com.kuaidaoresume.common.auth.AuthConstant;
import com.kuaidaoresume.common.auth.AuthContext;
//import com.kuaidaoresume.common.auth.Authorize;
//import com.kuaidaoresume.common.auth.PermissionDeniedException;
import com.kuaidaoresume.common.env.EnvConfig;
import com.kuaidaoresume.common.env.EnvConstant;
import com.kuaidaoresume.common.error.ServiceException;
//import com.kuaidaoresume.common.validation.PhoneNumber;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/v1/account")
@Validated
public class AccountController {

    static final ILogger logger = SLoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    //@Autowired
    //private EnvConfig envConfig;

    @GetMapping(path = "/get")
    @Authorize(value = {
            //AuthConstant.AUTHORIZATION_WWW_SERVICE,
            AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE,
            //AuthConstant.AUTHORIZATION_WHOAMI_SERVICE,
            //AuthConstant.AUTHORIZATION_BOT_SERVICE,
            //AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
            //AuthConstant.AUTHORIZATION_SUPPORT_USER,
            //AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    public GenericAccountResponse getAccount(@RequestParam @NotBlank String userId) {
        //this.validateAuthenticatedUser(userId);
        //this.validateEnv();

        AccountDto accountDto = accountService.get(userId);

        GenericAccountResponse genericAccountResponse = new GenericAccountResponse(accountDto);
        return genericAccountResponse;
    }
/*
    private void validateAuthenticatedUser(String userId) {
        if (AuthConstant.AUTHORIZATION_AUTHENTICATED_USER.equals(AuthContext.getAuthz())) {
            String currentUserId = AuthContext.getUserId();
            if (StringUtils.isEmpty(currentUserId)) {
                throw new ServiceException("failed to find current user id");
            }
            if (!userId.equals(currentUserId)) {
                throw new PermissionDeniedException("You do not have access to this service");
            }
        }
    }

    private void validateEnv() {
        if (AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE.equals(AuthContext.getAuthz())) {
            if (!EnvConstant.ENV_DEV.equals(this.envConfig.getName())) {
                logger.warn("Development service trying to connect outside development environment");
                throw new PermissionDeniedException("This service is not available outside development environments");
            }
        }
    }

 */
}
