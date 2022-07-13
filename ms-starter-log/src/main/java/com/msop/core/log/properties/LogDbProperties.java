package com.msop.core.log.properties;


import com.zaxxer.hikari.HikariConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 日志数据源配置
 * logType=db时生效(非必要)，如果不配置则使用当前数据源
 * @author ruozhuliufeng
 * @date 2021-07-17
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "ms.audit.log.datasource")
public class LogDbProperties extends HikariConfig {
}
