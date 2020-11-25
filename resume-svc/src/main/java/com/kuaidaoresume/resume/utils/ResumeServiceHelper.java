package com.kuaidaoresume.resume.utils;

import com.github.structlog4j.ILogger;
import com.kuaidaoresume.common.env.EnvConfig;
import io.sentry.SentryClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ResumeServiceHelper {

    private final SentryClient sentryClient;

    private final EnvConfig envConfig;

    public void handleException(ILogger log, Exception ex, String errMsg) {
        log.error(errMsg, ex);
        //if (!envConfig.isDebug()) {
        sentryClient.sendException(ex);
        //}
    }
}
