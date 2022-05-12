package com.msop.launch.config;

import com.msop.launch.properties.MsProperties;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * 配置类
 *
 * @author ruozhuliufeng
 */
@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
@EnableConfigurationProperties({MsProperties.class})
public class MsLaunchConfiguration {
}
