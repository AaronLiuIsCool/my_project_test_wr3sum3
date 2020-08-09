package com.kuaidaoresume.whoami.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import com.kuaidaoresume.common.auth.AuthConstant;
import com.kuaidaoresume.whoami.WhoAmIConstant;
import com.kuaidaoresume.whoami.dto.FindWhoAmIResponse;

@FeignClient(name = WhoAmIConstant.SERVICE_NAME, path = "/v1", url = "${kuaidaoresume.whoami-service-endpoint}")
public interface WhoAmIClient {
    @GetMapping
    FindWhoAmIResponse findWhoAmI(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authz);

    // TODO: Aaron Liu may not be using intercom for CRM Woody's conversation POC.
    // @GetMapping(value = "/intercom")
    //GetIntercomSettingResponse getIntercomSettings(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authz);
}
