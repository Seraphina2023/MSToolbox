package com.msop.core.log.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 审计日志配置
 * @RefreshScope 实现配置、类热部署
 * @author ruozhuliufeng
 * @date 2021-07-17
 */
@Setter
@Getter
@RefreshScope
@ConfigurationProperties(prefix = "ms.audit.log")
public class AuditLogProperties {
    /**
     * 是否开启审计日志
     */
    private Boolean enabled = false;
    /**
     * 日志记录类型(logger/redis/db/es)
     */
    private String logType;
}
