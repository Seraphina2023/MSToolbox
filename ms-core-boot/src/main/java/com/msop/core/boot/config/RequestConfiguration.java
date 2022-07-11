package com.msop.core.boot.config;

import com.msop.core.boot.request.MsRequestFilter;
import com.msop.core.boot.request.RequestProperties;
import com.msop.core.boot.request.XssProperties;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import javax.servlet.DispatcherType;

/**
 * 过滤器配置类
 *
 * @author ruozhuliufeng
 */
@AllArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({RequestProperties.class, XssProperties.class})
public class RequestConfiguration {
    private final RequestProperties requestProperties;
    private final XssProperties xssProperties;

    @Bean
    public FilterRegistrationBean<MsRequestFilter> msFilterRegistraion() {
        FilterRegistrationBean<MsRequestFilter> registration = new FilterRegistrationBean<>();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new MsRequestFilter(requestProperties, xssProperties));
        registration.addUrlPatterns("/*");
        registration.setName("msRequestFilter");
        registration.setOrder(Ordered.LOWEST_PRECEDENCE);
        return registration;
    }

}
