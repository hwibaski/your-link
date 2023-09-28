package com.yourink.config.chace;

import lombok.Getter;

@Getter
public enum CacheType {
    LINK_COUNT("linkCount");

    private final String cacheName;
    private final int expiredAfterWriteSecond;
    private final int maximumSize;

    CacheType(String cacheName) {

        this.cacheName = cacheName;
        this.expiredAfterWriteSecond = ConstConfig.DEFAULT_TTL;
        this.maximumSize = ConstConfig.DEFAULT_MAX_SIZE;
    }

    static class ConstConfig {
        static final int DEFAULT_TTL = 5 * 60;
        static final int DEFAULT_MAX_SIZE = 100;
    }
}
