package com.msop.core.context.config;

import com.msop.core.context.MsContext;
import com.msop.core.context.MsHttpHeadersGetter;
import com.msop.core.context.MsServletContext;
import com.msop.core.context.ServletHttpHeadersGetter;
import com.msop.core.context.properties.MsContextProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * Ms 服务上下文配置
 *
 * @author ruozhuliufeng
 */
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
@EnableConfigurationProperties(MsContextProperties.class)
public class MsContextAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public MsHttpHeadersGetter msHttpHeadersGetter(MsContextProperties contextProperties) {
        return new ServletHttpHeadersGetter(contextProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public MsContext msContext(MsContextProperties contextProperties, MsHttpHeadersGetter httpHeadersGetter) {
        return new MsServletContext(contextProperties, httpHeadersGetter);
    }
}
