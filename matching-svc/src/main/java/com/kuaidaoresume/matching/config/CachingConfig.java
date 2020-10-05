package com.kuaidaoresume.matching.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CachingConfig {

    @Value("${kuaidaoresume.matching.caching.maximum-size}")
    private long maximumSize;

    @Bean
    public Caffeine caffeineConfig() {
        return Caffeine.newBuilder().maximumSize(maximumSize);
    }

    @Bean
    public CacheManager cacheManager(Caffeine caffeine) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("matchedJobs", "tailoredJob",
            "bookmarkedJobs", "bookmarkedJobs-paging", "searchJobs", "searchJobs-paging");
        cacheManager.setCaffeine(caffeine);
        return cacheManager;
    }
}
