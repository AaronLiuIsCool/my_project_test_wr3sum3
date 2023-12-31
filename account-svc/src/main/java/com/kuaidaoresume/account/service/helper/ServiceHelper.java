package com.kuaidaoresume.account.service.helper;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import com.kuaidaoresume.account.config.AppConfig;
import com.kuaidaoresume.account.model.Account;
import com.kuaidaoresume.account.repo.AccountRepo;
import com.kuaidaoresume.common.env.EnvConfig;
import com.kuaidaoresume.common.error.ResourceNotFoundException;
import com.kuaidaoresume.common.error.ServiceException;
import io.intercom.api.Avatar;
import io.intercom.api.CustomAttribute;
import io.intercom.api.Event;
import io.intercom.api.User;
import io.sentry.SentryClient;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class ServiceHelper {
    static final ILogger logger = SLoggerFactory.getLogger(ServiceHelper.class);

    private final AccountRepo accountRepo;

    private final SentryClient sentryClient;

    private final EnvConfig envConfig;

    @Async(AppConfig.ASYNC_EXECUTOR_NAME)
    public void syncUserAsync(String userId) {
        if (envConfig.isDebug()) {
            logger.debug("syncUserAsync in dev & test environment");
            return;
        }

        Account account = accountRepo.findAccountById(userId);
        if (account == null) {
            throw new ResourceNotFoundException(String.format("User with id %s not found", userId));
        }
        //if (StringUtils.isEmpty(account.getPhoneNumber()) && StringUtils.isEmpty(account.getEmail())) { not for phase I TODO:Woody
        if (StringUtils.isEmpty(account.getEmail())) {
            logger.info(String.format("skipping sync for user %s because no email", account.getId()));
            return;
        }

        // resumes sync as necessary for this account.
        User user = new User();
        user.setUserId(account.getId());
        user.setEmail(account.getEmail());
        user.setName(account.getName());
        user.setSignedUpAt(account.getMemberSince().toEpochMilli());
        user.setAvatar(new Avatar().setImageURL(account.getPhotoUrl()));
        user.setLastRequestAt(Instant.now().toEpochMilli());

        user.addCustomAttribute(CustomAttribute.newBooleanAttribute("v2", true));
        //user.addCustomAttribute(CustomAttribute.newStringAttribute("phonenumber", account.getPhoneNumber())); not for phase I TODO:Woody
        user.addCustomAttribute(CustomAttribute.newBooleanAttribute("confirmed_and_active", account.isConfirmedAndActive()));
        user.addCustomAttribute(CustomAttribute.newBooleanAttribute("is_regular_user", true));
        //TODO: Aaron Liu, may not need in phase I
        // user.addCustomAttribute(CustomAttribute.newBooleanAttribute("is_admin", isAdmin));
        user.addCustomAttribute(CustomAttribute.newBooleanAttribute("is_kuaidaoresume_support", account.isSupport()));

        //TODO: Aaron Liu, may not need in phase I. this.syncUserWithIntercom(user, account.getId());
    }

/*    void syncUserWithIntercom(User user, String userId) {
        try {
            Map<String, String> params = Maps.newHashMap();
            params.put("user_id", userId);
            User existing = User.find(params);
            if (existing != null) {
                User.update(user);
            } else {
                User.create(user);
            }
            logger.debug("updated intercom");
        } catch (Exception ex) {
            String errMsg = "fail to create/update user on Intercom";
            handleException(logger, ex, errMsg);
            throw new ServiceException(errMsg, ex);
        }
    }*/

    @Async(AppConfig.ASYNC_EXECUTOR_NAME)
    public void trackEventAsync(String userId, String eventName) {
        if (envConfig.isDebug()) {
            logger.debug("intercom disabled in dev & test environment");
            return;
        }

        Event event = new Event()
                .setUserID(userId)
                .setEventName("v2_" + eventName)
                .setCreatedAt(Instant.now().toEpochMilli());

        try {
            Event.create(event);
        } catch (Exception ex) {
            String errMsg = "fail to create event on Intercom";
            handleException(logger, ex, errMsg);
            throw new ServiceException(errMsg, ex);
        }

        logger.debug("updated intercom");
    }

    // for time diff < 2s, treat them as almost same
    public boolean isAlmostSameInstant(Instant dt1, Instant dt2) {
        long diff = dt1.toEpochMilli() - dt2.toEpochMilli();
        diff = Math.abs(diff);
        if (diff < TimeUnit.SECONDS.toMillis(1)) {
            return true;
        }
        return false;
    }

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
