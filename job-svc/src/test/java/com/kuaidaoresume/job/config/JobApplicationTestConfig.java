package com.kuaidaoresume.job.config;

import org.springframework.context.annotation.Import;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
@Import(value = {AppConfig.class})
public class JobApplicationTestConfig {
}
