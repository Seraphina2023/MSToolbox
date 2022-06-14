package com.msop.core.cache.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * Cache 配置类
 *
 * @author ruozhuliufeng
 */
@EnableCaching
@Configuration(proxyBeanMethods = false)
public class CacheConfiguration {
}
