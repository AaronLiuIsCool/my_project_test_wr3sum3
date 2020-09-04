package com.kuaidaoresume.matching;

import com.kuaidaoresume.matching.repo.JobRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

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