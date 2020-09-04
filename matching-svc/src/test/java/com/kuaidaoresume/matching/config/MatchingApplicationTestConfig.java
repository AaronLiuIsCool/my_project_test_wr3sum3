package com.kuaidaoresume.matching.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@TestConfiguration
@EnableMongoRepositories(basePackages = "com.kuaidaoresume.matching.repo")
@EnableAutoConfiguration
public class MatchingApplicationTestConfig {
}
