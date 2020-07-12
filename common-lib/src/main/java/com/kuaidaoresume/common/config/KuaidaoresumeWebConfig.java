package com.kuaidaoresume.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import com.kuaidaoresume.common.aop.SentryClientAspect;

/**
 * Use this common config for Web App
 *
 * @author Aaron Liu
 */
@Configuration
@Import(value = {KuaidaoresumeConfig.class, SentryClientAspect.class,})
public class KuaidaoresumeWebConfig {
}
