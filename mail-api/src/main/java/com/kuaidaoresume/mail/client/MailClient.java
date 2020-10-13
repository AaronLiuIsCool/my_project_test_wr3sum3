package com.kuaidaoresume.mail.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.kuaidaoresume.common.api.BaseResponse;
import com.kuaidaoresume.mail.MailConstant;
import com.kuaidaoresume.mail.dto.EmailRequest;

import javax.validation.Valid;

@FeignClient(name = MailConstant.SERVICE_NAME, path = "/v1", url = "${kuaidaoresume.email-service-endpoint}")
@Profile("!it")
public interface MailClient {
    @PostMapping(path = "/send")
    BaseResponse send(@RequestBody @Valid EmailRequest request);
}
