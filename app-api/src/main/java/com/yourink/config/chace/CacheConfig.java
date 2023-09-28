package com.yourink.config.chace;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();

        List<CaffeineCache> caches = Arrays.stream(CacheType.values())
                                           .map(cacheType ->
                                                        new CaffeineCache(cacheType.getCacheName(), Caffeine.newBuilder()
                                                                                                            .expireAfterWrite(cacheType.getExpiredAfterWriteSecond(), java.util.concurrent.TimeUnit.SECONDS)
                                                                                                            .maximumSize(cacheType.getMaximumSize())
                                                                                                            .build())
                                           ).toList();
        simpleCacheManager.setCaches(caches);

        return simpleCacheManager;
    }
}

