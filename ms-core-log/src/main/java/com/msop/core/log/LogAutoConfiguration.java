package com.msop.core.log;

import com.msop.core.log.properties.AuditLogProperties;
import com.msop.core.log.properties.LogDbProperties;
import com.msop.core.log.properties.TraceProperties;
import com.zaxxer.hikari.HikariConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 日志自动配置
 * @author ruozhuliufeng
 * @date 2021-07-17
 */
@ComponentScan
@EnableConfigurationProperties({AuditLogProperties.class, TraceProperties.class})
public class LogAutoConfiguration {


    @Configuration
    @ConditionalOnClass(HikariConfig.class)
    @EnableConfigurationProperties(LogDbProperties.class)
    public static class LogDbAutoConfigure{}

}
