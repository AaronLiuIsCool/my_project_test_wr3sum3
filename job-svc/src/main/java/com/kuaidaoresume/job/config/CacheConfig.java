package com.kuaidaoresume.job.config;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MaxSizePolicy;
import com.hazelcast.config.InMemoryFormat;
import com.kuaidaoresume.job.controller.v1.JobController;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;

@Configuration
@Getter
@EnableCaching
public class CacheConfig {
    static final ILogger logger = SLoggerFactory.getLogger(CacheConfig.class);

    public static final String JOB_CACHE = "job_cache";
    public static final String JOB_SEARCH_CACHE = "job_search_cache";

    @Value("${kuaidaoresume.cache.job.policy.max-size-policy}")
    private String JOB_CACHE_POLICY_MAXSIZE;

    @Value("${kuaidaoresume.cache.job.policy.eviction-policy}")
    private String JOB_SEARCH_CACHE_POLICY_MAXSIZE;

    @Value("${kuaidaoresume.cache.job-search.policy.max-size-policy}")
    private String JOB_CACHE_POLICY_EVICTION;

    @Value("${kuaidaoresume.cache.job-search.policy.eviction-policy}")
    private String JOB_SEARCH_CACHE_POLICY_EVICTION;

    /**
     * @see <a href="https://docs.spring.io/spring-boot/docs/2.0.x/reference/html/boot-features-hazelcast.html">https://docs.spring.io/spring-boot/docs/2.0.x/reference/html/boot-features-hazelcast.html</a>
     */
    @Bean
    Config config() {
        Config config = new Config();

        MapConfig jobCacheConfig = new MapConfig();

        EvictionConfig jobCacheEvictionConfig = new EvictionConfig();
        setEvictionPolicy(jobCacheEvictionConfig, JOB_CACHE_POLICY_EVICTION);
        setMaxSizePolicy(jobCacheEvictionConfig, JOB_CACHE_POLICY_MAXSIZE); // https://docs.hazelcast.org/docs/latest/javadoc/com/hazelcast/config/MaxSizePolicy.html
        jobCacheConfig.setEvictionConfig(jobCacheEvictionConfig);

        jobCacheConfig.setInMemoryFormat(InMemoryFormat.BINARY);  // https://hazelcast.org/blog/in-memory-format/
        config.getMapConfigs().put(JOB_CACHE, jobCacheConfig);


        MapConfig jobSearchCacheConfig = new MapConfig();

        EvictionConfig searchEvictionConfig = new EvictionConfig();
        setEvictionPolicy(searchEvictionConfig, JOB_SEARCH_CACHE_POLICY_EVICTION);
        setMaxSizePolicy(jobCacheEvictionConfig, JOB_SEARCH_CACHE_POLICY_MAXSIZE);
        jobSearchCacheConfig.setEvictionConfig(searchEvictionConfig);

        jobSearchCacheConfig.setInMemoryFormat(InMemoryFormat.BINARY);
        config.getMapConfigs().put(JOB_SEARCH_CACHE, jobSearchCacheConfig);


        NetworkConfig networkConfig = config.getNetworkConfig();
        networkConfig.setPortAutoIncrement(true); // https://stackoverflow.com/questions/25682890/purpose-of-auto-increment-flag-in-hazelcast-config

        JoinConfig join = networkConfig.getJoin();
        join.getMulticastConfig().setEnabled(false);
        join.getTcpIpConfig().setEnabled(true); // https://docs.hazelcast.org/docs/4.0.3/javadoc/com/hazelcast/config/JoinConfig.html

        logger.info("JOB_CACHE_POLICY_EVICTION =  " + JOB_CACHE_POLICY_EVICTION);
        logger.info("JOB_CACHE_POLICY_MAXSIZE =  " + JOB_CACHE_POLICY_MAXSIZE);
        logger.info("JOB_SEARCH_CACHE_POLICY_EVICTION =  " + JOB_SEARCH_CACHE_POLICY_EVICTION);
        logger.info("JOB_SEARCH_CACHE_POLICY_MAXSIZE =  " + JOB_SEARCH_CACHE_POLICY_MAXSIZE);

        return config;
    }

    private void setEvictionPolicy(EvictionConfig cacheEvictionConfig, String policy) {
        switch (policy) {
            case "LRU":
                cacheEvictionConfig.setEvictionPolicy(EvictionPolicy.LRU);
            case "LFU":
                cacheEvictionConfig.setEvictionPolicy(EvictionPolicy.LFU);
            default:
                cacheEvictionConfig.setEvictionPolicy(EvictionPolicy.RANDOM);
        }
    }

    private void setMaxSizePolicy(EvictionConfig cacheEvictionConfig, String policy) {
        switch (policy) {
            case "ENTRY_COUNT":
                cacheEvictionConfig.setMaxSizePolicy(MaxSizePolicy.ENTRY_COUNT);
            case "FREE_HEAP_PERCENTAGE":
                cacheEvictionConfig.setMaxSizePolicy(MaxSizePolicy.FREE_HEAP_PERCENTAGE);
            case "FREE_HEAP_SIZE":
                cacheEvictionConfig.setMaxSizePolicy(MaxSizePolicy.FREE_HEAP_SIZE);
            case "FREE_NATIVE_MEMORY_PERCENTAGE":
                cacheEvictionConfig.setMaxSizePolicy(MaxSizePolicy.FREE_NATIVE_MEMORY_PERCENTAGE);
            case "FREE_NATIVE_MEMORY_SIZE":
                cacheEvictionConfig.setMaxSizePolicy(MaxSizePolicy.FREE_NATIVE_MEMORY_SIZE);
            case "PER_PARTITION":
                cacheEvictionConfig.setMaxSizePolicy(MaxSizePolicy.PER_PARTITION);
            case "USED_HEAP_PERCENTAGE":
                cacheEvictionConfig.setMaxSizePolicy(MaxSizePolicy.USED_HEAP_PERCENTAGE);
            case "USED_HEAP_SIZE":
                cacheEvictionConfig.setMaxSizePolicy(MaxSizePolicy.USED_HEAP_SIZE);
            case "USED_NATIVE_MEMORY_PERCENTAGE":
                cacheEvictionConfig.setMaxSizePolicy(MaxSizePolicy.USED_NATIVE_MEMORY_PERCENTAGE);
            case "USED_NATIVE_MEMORY_SIZE":
                cacheEvictionConfig.setMaxSizePolicy(MaxSizePolicy.USED_NATIVE_MEMORY_SIZE);
            default:
                cacheEvictionConfig.setMaxSizePolicy(MaxSizePolicy.PER_NODE);
        }
    }

}
