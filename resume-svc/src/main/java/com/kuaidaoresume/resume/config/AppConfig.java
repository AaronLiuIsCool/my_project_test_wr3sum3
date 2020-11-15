package com.kuaidaoresume.resume.config;

import com.github.pemistahl.lingua.api.LanguageDetector;
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder;
import com.kuaidaoresume.common.async.ContextCopyingDecorator;
import com.kuaidaoresume.common.config.KuaidaoresumeRestConfig;
import com.kuaidaoresume.common.matching.*;
import com.kuaidaoresume.common.utils.FileUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.hal.HalLinkDiscoverer;
import org.springframework.hateoas.server.LinkRelationProvider;
import org.springframework.hateoas.server.core.EvoInflectorLinkRelationProvider;
import org.springframework.plugin.core.SimplePluginRegistry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;

import static com.github.pemistahl.lingua.api.IsoCode639_1.EN;
import static com.github.pemistahl.lingua.api.IsoCode639_1.ZH;

@Configuration
@EnableAsync
@Import(value = {KuaidaoresumeRestConfig.class})
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

    @Bean
    public LinkDiscoverers discoverers() {
        List<LinkDiscoverer> plugins = new ArrayList<>();
        plugins.add(new HalLinkDiscoverer());
        return new LinkDiscoverers(SimplePluginRegistry.create(plugins));
    }

    @Bean
    public LinkRelationProvider provider() {
        return new EvoInflectorLinkRelationProvider();
    }

    @Bean
    public LanguageDetector languageDetector() {
        return LanguageDetectorBuilder.fromIsoCodes639_1(EN, ZH).build();
    }

    @Bean
    public KeywordMatcher keywordMatcher() {
        Collection<String> keywords = FileUtil.getKeywordsFromExcel("matching/keywords.xlsx");
        keywords.addAll(FileUtil.getKeywordsFromExcel("matching/Chinese_keywords.xlsx"));
        return new InMemoryKeywordMatcher(keywords);
    }

    @Bean
    public NumericWordMatcher numericWordMatcher() {
        return new NumericWordMatcherImpl(false);
    }

    @Bean
    public NameMatcher topTierUniversitiesMatcher() {
        return new NameMatcher(FileUtil.loadLinesFromTextFile("matching/top-tier-universities.txt"));
    }

    @Bean
    public NameMatcher secondTierUniversitiesMatcher() {
        return new NameMatcher(FileUtil.loadLinesFromTextFile("matching/second-tier-universities.txt"));
    }

    @Bean
    public NameMatcher topTierCompaniesMatcher() {
        return new NameMatcher(FileUtil.loadLinesFromTextFile("matching/top-tier-companies.txt"));
    }

    @Bean
    public NameMatcher secondTierCompaniesMatcher() {
        return new NameMatcher(FileUtil.loadLinesFromTextFile("matching/second-tier-companies.txt"));
    }
}
