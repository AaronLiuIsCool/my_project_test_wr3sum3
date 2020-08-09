package com.kuaidaoresume.whoami;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import com.kuaidaoresume.common.config.KuaidaoresumeRestConfig;

@Import(value = KuaidaoresumeRestConfig.class)
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients(basePackages = {"com.kuaidaoresume.job","com.kuaidaoresume.resume", "com.kuaidaoresume.account"})
public class WhoAmIApplication {
public static void main(String[] args) {
        SpringApplication.run(WhoAmIApplication.class, args);
}
}