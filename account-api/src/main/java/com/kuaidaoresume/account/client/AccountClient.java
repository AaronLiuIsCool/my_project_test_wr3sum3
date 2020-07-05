package com.kuaidaoresume.account.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.kuaidaoresume.account.AccountConstant;
import com.kuaidaoresume.account.dto.*;
import com.kuaidaoresume.common.api.BaseResponse;
//import com.kuaidaoresume.common.auth.AuthConstant;
//import com.kuaidaoresume.common.validation.Group1;
//import com.kuaidaoresume.common.validation.PhoneNumber;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@FeignClient(name = AccountConstant.SERVICE_NAME, path = "/v1/account", url = "${kuaidao.account-service-endpoint}")
public interface AccountClient {

    @GetMapping(path = "/get")
    BaseResponse getAccount(@RequestParam @NotBlank String userId);

    //@GetMapping(path = "/list")
    //ListAccountResponse listAccounts(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authz, @RequestParam int offset, @RequestParam @Min(0) int limit);

}
