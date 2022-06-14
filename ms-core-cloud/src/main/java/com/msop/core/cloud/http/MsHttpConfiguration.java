package com.msop.core.cloud.http;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Http配置
 *
 * @author ruozhuliufeng
 */
@Configuration
@EnableConfigurationProperties(MsHttpProperties.class)
public class MsHttpConfiguration {
}
