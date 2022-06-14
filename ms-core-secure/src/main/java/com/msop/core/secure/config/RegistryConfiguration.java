package com.msop.core.secure.config;

import com.msop.core.secure.handler.IPermissionHandler;
import com.msop.core.secure.handler.ISecureHandler;
import com.msop.core.secure.handler.MsPermissionHandler;
import com.msop.core.secure.handler.SecureHandler;
import com.msop.core.secure.registry.SecureRegistry;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Secure 注册默认配置
 *
 * @author ruozhuliufeng
 */
@Order
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(SecureConfiguration.class)
@AllArgsConstructor
public class RegistryConfiguration {
    private final JdbcTemplate jdbcTemplate;

    @Bean
    @ConditionalOnMissingBean(SecureRegistry.class)
    public SecureRegistry secureRegistry() {
        return new SecureRegistry();
    }

    @Bean
    @ConditionalOnMissingBean(ISecureHandler.class)
    public ISecureHandler secureHandler(){
        return new SecureHandler();
    }
    @Bean
    @ConditionalOnMissingBean(IPermissionHandler.class)
    public IPermissionHandler permissionHandler(){
        return new MsPermissionHandler(jdbcTemplate);
    }
}
