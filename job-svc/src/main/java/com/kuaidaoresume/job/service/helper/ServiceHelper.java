package com.kuaidaoresume.job.service.helper;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import com.kuaidaoresume.common.env.EnvConfig;
import io.sentry.SentryClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ServiceHelper {
    static final ILogger logger = SLoggerFactory.getLogger(ServiceHelper.class);

    private final SentryClient sentryClient;

    @Autowired
    private EnvConfig envConfig;

    // TODO Ruichen, please use below utilities for sentry exception logging as part of Job service failover jira task.
    public void handleError(ILogger log, String errMsg) {
        log.error(errMsg);
        if (!envConfig.isDebug()) {
            sentryClient.sendMessage(errMsg);
        }
    }

    public void handleException(ILogger log, Exception ex, String errMsg) {
        log.error(errMsg, ex);
        if (!envConfig.isDebug()) {
            sentryClient.sendMessage(errMsg);
            sentryClient.sendException(ex);
        }
    }
}
