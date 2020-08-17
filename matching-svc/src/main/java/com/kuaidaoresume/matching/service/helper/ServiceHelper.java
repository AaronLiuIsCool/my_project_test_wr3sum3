package com.kuaidaoresume.matching.service.helper;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import io.sentry.SentryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.kuaidaoresume.common.api.BaseResponse;
import com.kuaidaoresume.common.auth.AuthContext;
import com.kuaidaoresume.common.env.EnvConfig;
import com.kuaidaoresume.common.error.ServiceException;
import com.kuaidaoresume.matching.config.AppConfig;
import com.kuaidaoresume.account.client.AccountClient;
import com.kuaidaoresume.account.dto.TrackEventRequest;

@Component
public class ServiceHelper {

    static final ILogger logger = SLoggerFactory.getLogger(ServiceHelper.class);

    @Autowired
    private AccountClient accountClient;

    @Autowired
    private SentryClient sentryClient;

    @Autowired
    private EnvConfig envConfig;

    @Async(AppConfig.ASYNC_EXECUTOR_NAME)
    public void trackEventAsync(String event) {

        String userId = AuthContext.getUserId();
        if (StringUtils.isEmpty(userId)) {
            // Not an action performed by a normal user
            // (noop - not an view)
            return;
        }

        TrackEventRequest trackEventRequest = TrackEventRequest.builder()
                .userId(userId).event(event).build();

        BaseResponse resp = null;
        try {
            resp = accountClient.trackEvent(trackEventRequest);
        } catch (Exception ex) {
            String errMsg = "fail to trackEvent through accountClient";
            handleErrorAndThrowException(logger, ex, errMsg);
        }
        if (!resp.isSuccess()) {
            handleErrorAndThrowException(logger, resp.getMessage());
        }
    }

    public void handleErrorAndThrowException(ILogger log, String errMsg) {
        log.error(errMsg);
        if (!envConfig.isDebug()) {
            sentryClient.sendMessage(errMsg);
        }
        throw new ServiceException(errMsg);
    }

    public void handleErrorAndThrowException(ILogger log, Exception ex, String errMsg) {
        log.error(errMsg, ex);
        if (!envConfig.isDebug()) {
            sentryClient.sendException(ex);
        }
        throw new ServiceException(errMsg, ex);
    }
}
