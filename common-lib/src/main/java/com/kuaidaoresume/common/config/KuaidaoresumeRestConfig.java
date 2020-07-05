package com.kuaidaoresume.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import com.kuaidaoresume.common.error.GlobalExceptionTranslator;
import com.kuaidaoresume.common.aop.*;

/**
 * Use this common config for Rest API
 *
 * @author Aaron Liu
 */
@Configuration
@Import(value = {KuaidaoresumeConfig.class, SentryClientAspect.class, GlobalExceptionTranslator.class})
public class KuaidaoresumeRestConfig {
}
