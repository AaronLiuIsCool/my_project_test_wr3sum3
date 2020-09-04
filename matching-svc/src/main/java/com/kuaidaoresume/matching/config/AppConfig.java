package com.kuaidaoresume.matching.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.plugin.core.SimplePluginRegistry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.kuaidaoresume.common.async.ContextCopyingDecorator;
import com.kuaidaoresume.common.config.KuaidaoresumeRestConfig;
import org.springframework.hateoas.client.JsonPathLinkDiscoverer;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.hal.HalLinkDiscoverer;

import java.util.concurrent.Executor;
import java.util.List;
import java.util.ArrayList;

@Configuration
@EnableAsync
@Import(value = {KuaidaoresumeRestConfig.class})
@EnableMongoRepositories(basePackages = "com.kuaidaoresume.matching.repo")
@EnableAutoConfiguration
@SuppressWarnings(value = "Duplicates")
public class AppConfig {

    public static final String ASYNC_EXECUTOR_NAME = "asyncExecutor";

    @Bean(name = ASYNC_EXECUTOR_NAME)
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // for passing in request scope context
        executor.setTaskDecorator(new ContextCopyingDecorator());
        executor.setCorePoolSize(3); // TODO Aaron add to properties
        executor.setMaxPoolSize(5); // TODO Aaron add to properties
        executor.setQueueCapacity(100); // TODO Aaron add to properties
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("AsyncThread-");
        executor.initialize();
        return executor;
    }

    /**
     * @see <a href="https://stackoverflow.com/questions/58431876/spring-boot-2-2-0-spring-hateoas-startup-issue">https://stackoverflow.com/questions/58431876/spring-boot-2-2-0-spring-hateoas-startup-issue</a>
     * @see <a href="https://dev.to/otaviotarelhodb/how-to-use-springfox-2-9-2-with-spring-hateoas-2-on-gradle-project-6mn">https://dev.to/otaviotarelhodb/how-to-use-springfox-2-9-2-with-spring-hateoas-2-on-gradle-project-6mn</a>
     */
    @Bean
    public LinkDiscoverers discoverers() {
        List<JsonPathLinkDiscoverer> plugins = new ArrayList<>();
        plugins.add(new HalLinkDiscoverer());
        return new LinkDiscoverers(SimplePluginRegistry.create(plugins));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


