package com.kuaidaoresume.job.service.helper;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import io.sentry.SentryClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ServiceHelper {
    static final ILogger logger = SLoggerFactory.getLogger(ServiceHelper.class);

    private final SentryClient sentryClient;

    public void handleError(ILogger log, String errMsg) {
        log.error(errMsg);
        //if (!envConfig.isDebug()) {
        sentryClient.sendMessage(errMsg);
        //}
    }

    public void handleException(ILogger log, Exception ex, String errMsg) {
        log.error(errMsg, ex);
        //if (!envConfig.isDebug()) {
        sentryClient.sendMessage(errMsg);
        sentryClient.sendException(ex);
        //}
    }
}
