package com.msop.core.redis.properties;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 缓存管理
 */
@Getter
@Slf4j
@Setter
@ConfigurationProperties(prefix = "ossp.cache-manage")
public class CacheManagerProperties {

    private List<CacheConfig> configs;

    @Getter
    @Setter
    public static class CacheConfig{

        /**
         * 缓存key
         */
        private String key;
        /**
         * 过期时间 sec
         */
        private long second = 0;
    }
}
