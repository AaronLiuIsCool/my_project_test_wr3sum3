package com.kuaidaoresume.matching.service.helper;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import com.kuaidaoresume.common.env.EnvConfig;
import com.kuaidaoresume.common.error.ServiceException;
import com.kuaidaoresume.matching.config.AppConfig;
import io.sentry.SentryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class ServiceHelper {

    static final ILogger logger = SLoggerFactory.getLogger(ServiceHelper.class);

    // TODO add it back when integrating CRM
//    @Autowired
//    private AccountClient accountClient;

    @Autowired
    private SentryClient sentryClient;

    @Autowired
    private EnvConfig envConfig;

    private ExecutorService executor;

    @PostConstruct
    public void init() {
        executor = Executors.newFixedThreadPool(8);
    }

    @PreDestroy
    public void tearDown() {
        if (executor != null) {
            try {
                executor.shutdown();
                executor.awaitTermination(2, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                String errorMessage = "Failed to shutdown ExecutorService.";
                logger.warn(errorMessage, e);
                sentryClient.sendMessage(errorMessage);
                sentryClient.sendException(e);
            }
        }
    }

    @Async(AppConfig.ASYNC_EXECUTOR_NAME)
    public void executeAsync(Runnable task) {
        executor.execute(task);
    }

    // TODO add it back when integrating CRM
//    @Async(AppConfig.ASYNC_EXECUTOR_NAME)
//    public void trackEventAsync(String event) {
//
//        String userId = AuthContext.getUserId();
//        if (StringUtils.isEmpty(userId)) {
//            // Not an action performed by a normal user
//            // (noop - not an view)
//            return;
//        }
//
//        TrackEventRequest trackEventRequest = TrackEventRequest.builder()
//                .userId(userId).event(event).build();
//
//        BaseResponse resp = null;
//        try {
//            resp = accountClient.trackEvent(trackEventRequest);
//        } catch (Exception ex) {
//            String errMsg = "fail to trackEvent through accountClient";
//            handleErrorAndThrowException(logger, ex, errMsg);
//        }
//        if (!resp.isSuccess()) {
//            handleErrorAndThrowException(logger, resp.getMessage());
//        }
//    }

    // TODO Spencer, please use below utilities for sentry exception logging.
    public void handleErrorAndThrowException(ILogger log, String errMsg) {
        log.error(errMsg);
        if (!envConfig.isDebug()) {
            sentryClient.sendMessage(errMsg);
        }
        throw new ServiceException(errMsg);
    }

    // TODO Spencer, please use below utilities for sentry exception logging.
    public void handleErrorAndThrowException(ILogger log, Exception ex, String errMsg) {
        handleError(log, ex, errMsg);
        throw new ServiceException(errMsg, ex);
    }

    public void handleError(ILogger log, Throwable throwable, String errMsg) {
        log.error(errMsg, throwable);
        if (!envConfig.isDebug()) {
            sentryClient.sendException(throwable);
        }
    }
}
