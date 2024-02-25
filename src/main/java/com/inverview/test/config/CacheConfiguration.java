package com.inverview.test.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfiguration {
    public final static String CACHE_TTL_CONFIG_KEY = "${caching.spring.ttlms}";

    @Value(CACHE_TTL_CONFIG_KEY)
    private Long cacheTtl;

    public Long getCacheTtl() {
        return cacheTtl;
    }
}
