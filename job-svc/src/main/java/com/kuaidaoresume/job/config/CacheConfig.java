package com.kuaidaoresume.job.config;

import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MaxSizePolicy;
import com.hazelcast.config.InMemoryFormat;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;

@Configuration
@EnableCaching
public class CacheConfig {
    public static final String JOB_CACHE = "job_cache";
    public static final String JOB_SEARCH_CACHE = "job_search_cache";

    /**
     * @see <a href="https://docs.spring.io/spring-boot/docs/2.0.x/reference/html/boot-features-hazelcast.html">https://docs.spring.io/spring-boot/docs/2.0.x/reference/html/boot-features-hazelcast.html</a>
     */
    @Bean
    Config config() {
        Config config = new Config();

        MapConfig jobCacheConfig = new MapConfig();

        EvictionConfig jobCacheEvictionConfig = new EvictionConfig();
        jobCacheEvictionConfig.setEvictionPolicy(EvictionPolicy.LRU);
        jobCacheEvictionConfig.setMaxSizePolicy(MaxSizePolicy.PER_NODE); // https://docs.hazelcast.org/docs/latest/javadoc/com/hazelcast/config/MaxSizePolicy.html
        jobCacheConfig.setEvictionConfig(jobCacheEvictionConfig);

        jobCacheConfig.setInMemoryFormat(InMemoryFormat.BINARY);  // https://hazelcast.org/blog/in-memory-format/
        config.getMapConfigs().put(JOB_CACHE, jobCacheConfig);


        MapConfig jobSearchCacheConfig = new MapConfig();

        EvictionConfig searchEvictionConfig = new EvictionConfig();
        searchEvictionConfig.setEvictionPolicy(EvictionPolicy.LFU);
        jobCacheEvictionConfig.setMaxSizePolicy(MaxSizePolicy.PER_NODE);
        jobSearchCacheConfig.setEvictionConfig(searchEvictionConfig);

        jobSearchCacheConfig.setInMemoryFormat(InMemoryFormat.BINARY);
        config.getMapConfigs().put(JOB_SEARCH_CACHE, jobSearchCacheConfig);


        NetworkConfig networkConfig = config.getNetworkConfig();
        networkConfig.setPortAutoIncrement(true); // https://stackoverflow.com/questions/25682890/purpose-of-auto-increment-flag-in-hazelcast-config

        JoinConfig join = networkConfig.getJoin();
        join.getMulticastConfig().setEnabled(false);
        join.getTcpIpConfig().setEnabled(true); // https://docs.hazelcast.org/docs/4.0.3/javadoc/com/hazelcast/config/JoinConfig.html


        return config;
    }

}
