package com.kuaidaoresume.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Account services main entry.
 * @author Aaron Liu.
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.kuaidaoresume.mail"})
public class AccountApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountApplication.class, args);
    }
}