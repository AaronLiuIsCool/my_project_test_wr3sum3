package com.kuaidaoresume.account.service.helper;

import io.sentry.SentryClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import com.kuaidaoresume.account.model.Account;
import com.kuaidaoresume.account.repo.AccountRepo;
import com.kuaidaoresume.common.env.EnvConfig;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class ServiceHelperTest {

    @Mock
    private AccountRepo accountRepo;

    @Mock
    private SentryClient sentryClient;

    @Mock
    private EnvConfig envConfig;

    @InjectMocks
    @Spy
    private ServiceHelper serviceHelper;

    @Test
    public void testIsAlmostSameInstant() {
        Instant now = Instant.now();
        Instant twoSecondsLater = now.plusSeconds(2);
        assertThat(serviceHelper.isAlmostSameInstant(now, twoSecondsLater)).isFalse();

        Instant oneSecondLater = now.plusSeconds(1);
        assertThat(serviceHelper.isAlmostSameInstant(now, oneSecondLater)).isFalse();

        Instant haveSecondLater = now.plus(500000, ChronoUnit.MICROS);
        assertThat(serviceHelper.isAlmostSameInstant(now, haveSecondLater)).isTrue();
    }

}
