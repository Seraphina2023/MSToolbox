package com.msop.core.context.config;

import com.msop.core.context.MsHttpHeadersGetter;
import com.msop.core.context.listener.MsServletRequestListener;
import com.msop.core.context.properties.MsContextProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Servlet 监听器自动配置
 *
 * @author ruozhuliufeng
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class MsServletListenerConfiguration {

    @Bean
    public ServletListenerRegistrationBean<?> registerCustomerListener(MsContextProperties properties,
                                                                       MsHttpHeadersGetter httpHeadersGetter) {
        return new ServletListenerRegistrationBean<>(new MsServletRequestListener(properties, httpHeadersGetter));
    }
}
