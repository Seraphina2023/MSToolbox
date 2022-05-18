package com.msop.core.secure.config;

import com.msop.core.secure.aspect.AuthAspect;
import com.msop.core.secure.interceptor.ClientInterceptor;
import com.msop.core.secure.interceptor.SecureInterceptor;
import com.msop.core.secure.properties.MsSecureProperties;
import com.msop.core.secure.provider.ClientDetailsServiceImpl;
import com.msop.core.secure.provider.IClientDetailsService;
import com.msop.core.secure.registry.SecureRegistry;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 安全配置类
 *
 * @author ruozhuliufeng
 */
@Order
@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@EnableConfigurationProperties({MsSecureProperties.class})
public class SecureConfiguration implements WebMvcConfigurer {

    private final SecureRegistry secureRegistry;
    private final MsSecureProperties secureProperties;

    private final JdbcTemplate jdbcTemplate;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        secureProperties.getClient()
                .forEach(cs -> registry.addInterceptor(new ClientInterceptor(cs.getClientId()))
                        .addPathPatterns(cs.getPathPatterns()));
        ;
        if (secureRegistry.isEnabled()) {
            registry.addInterceptor(new SecureInterceptor())
                    .excludePathPatterns(secureRegistry.getExcludePatterns())
                    .excludePathPatterns(secureRegistry.getDefaultExcludePatterns())
                    .excludePathPatterns(secureProperties.getSkipUrl());
        }
        WebMvcConfigurer.super.addInterceptors(registry);
    }

    @Bean
    public AuthAspect authAspect() {
        return new AuthAspect();
    }

    @Bean
    @ConditionalOnMissingBean(IClientDetailsService.class)
    public IClientDetailsService clientDetailsService() {
        return new ClientDetailsServiceImpl(jdbcTemplate);
    }

}
