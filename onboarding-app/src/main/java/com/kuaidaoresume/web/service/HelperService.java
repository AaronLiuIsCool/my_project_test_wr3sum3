package com.kuaidaoresume.web.service;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import io.sentry.SentryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.kuaidaoresume.account.client.AccountClient;
import com.kuaidaoresume.account.dto.AccountDto;
import com.kuaidaoresume.account.dto.SyncUserRequest;
import com.kuaidaoresume.account.dto.TrackEventRequest;
import com.kuaidaoresume.common.api.BaseResponse;
import com.kuaidaoresume.common.error.ServiceException;
import com.kuaidaoresume.mail.client.MailClient;
import com.kuaidaoresume.mail.dto.EmailRequest;
import com.kuaidaoresume.web.config.AppConfig;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class HelperService {

    static final ILogger logger = SLoggerFactory.getLogger(HelperService.class);

    static final String METHOD_POST = "POST";

    @Autowired
    AccountClient accountClient;

    @Autowired
    SentryClient sentryClient;

    @Autowired
    MailClient mailClient;

    public static boolean isPost(HttpServletRequest request) {
        return METHOD_POST.equals(request.getMethod());
    }

    public void logError(ILogger log, String errMsg) {
        log.error(errMsg);
        sentryClient.sendMessage(errMsg);
    }

    public void logException(ILogger log, Exception ex, String errMsg) {
        log.error(errMsg, ex);
        sentryClient.sendException(ex);
    }

    @Async(AppConfig.ASYNC_EXECUTOR_NAME)
    public void trackEventAsync(String userId, String event) {
        TrackEventRequest trackEventRequest = TrackEventRequest.builder()
                .userId(userId).event(event).build();
        BaseResponse baseResponse = null;
        try {
            baseResponse = accountClient.trackEvent(trackEventRequest);
        } catch (Exception ex) {
            String errMsg = "fail to trackEvent through accountClient";
            logException(logger, ex, errMsg);
        }
        if (!baseResponse.isSuccess()) {
            logError(logger, baseResponse.getMessage());
        }
    }

    @Async(AppConfig.ASYNC_EXECUTOR_NAME)
    public void syncUserAsync(String userId) {
        SyncUserRequest request = SyncUserRequest.builder().userId(userId).build();
        accountClient.syncUser(request);
    }

    @Async(AppConfig.ASYNC_EXECUTOR_NAME)
    public void sendEmailAsync(AccountDto a) {
        EmailRequest emailRequest = EmailRequest.builder()
                .to("sales@kuaidaoresume.com")
                .name("")
                .subject(String.format("%s from %s just joined Kuaidaoresume", a.getName()))
                // .htmlBody(String.format("Name: %s<br>Phone: %s<br>Email: %s<br>", not for phase I TODO:Woody
                .htmlBody(String.format("Name: %s <br>Email: %s<br>",
                        a.getName(),
                        // a.getPhoneNumber(), not for phase I TODO:Woody
                        a.getEmail()
                        ))
                .build();

        BaseResponse baseResponse = null;
        try {
            baseResponse = mailClient.send(emailRequest);
        } catch (Exception ex) {
            String errMsg = "Unable to send email";
            logException(logger, ex, errMsg);
        }
        if (!baseResponse.isSuccess()) {
            logError(logger, baseResponse.getMessage());
        }
    }

    public static String buildUrl(String scheme, String host) {
        return buildUrl(scheme, host, null);
    }

    public static String buildUrl(String scheme, String host, String path) {
        try {
            URI uri = new URI(scheme, host, path, null);
            return uri.toString();
        } catch (URISyntaxException ex) {
            String errMsg = "Internal uri parsing exception.";
            logger.error(errMsg);
            throw new ServiceException(errMsg, ex);
        }
    }
}