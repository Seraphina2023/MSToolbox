package com.msop.core.redis.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizers;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
/**
 * CacheManagerCustomizers 配置
 *
 * @author ruozhuliufeng
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(CacheManagerCustomizers.class)
public class RedisCacheManagerConfiguration {

    @Bean
    public CacheManagerCustomizers cacheManagerCustomizers(
            ObjectProvider<List<CacheManagerCustomizer<?>>> customizers
    ) {
        return new CacheManagerCustomizers(customizers.getIfAvailable());
    }
}
