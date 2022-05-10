package com.msop.core.common.config;

import com.msop.core.common.propertis.MsRequestProperties;
import com.msop.core.common.propertis.MsXssProperties;
import com.msop.core.common.request.MsRequestFilter;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import javax.servlet.DispatcherType;

@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@EnableConfigurationProperties({MsRequestProperties.class, MsXssProperties.class})
public class RequestConfiguration {
    private final MsRequestProperties requestProperties;
    private final MsXssProperties xssProperties;

    /**
     * 全局过滤器
     */
    @Bean
    public FilterRegistrationBean<MsRequestFilter> msFilterRegistration() {
        FilterRegistrationBean<MsRequestFilter> registration = new FilterRegistrationBean<>();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new MsRequestFilter(requestProperties, xssProperties));
        registration.addUrlPatterns("/*");
        registration.setName("msRequestFilter");
        registration.setOrder(Ordered.LOWEST_PRECEDENCE);
        return registration;
    }
}
