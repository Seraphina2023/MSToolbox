package com.msop.core.tenant.config;

import com.msop.core.mp.config.MybatisPlusConfiguration;
import com.msop.core.tenant.properties.MsTenantProperties;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 多租户配置类
 *
 * @author ruozhuliufeng
 */
@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@AutoConfigureBefore(MybatisPlusConfiguration.class)
@EnableConfigurationProperties(MsTenantProperties.class)
public class TenantConfiguration {
}
