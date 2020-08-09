package com.kuaidaoresume.matching;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Matching services main entry.
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.kuaidaoresume.job", "com.kuaidaoresume.resume"})
public class MatchingApplication {

    public static void main(String[] args) {
        SpringApplication.run(MatchingApplication.class, args);
    }
}