package tech.msop.core.context.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import tech.msop.core.context.MsContext;
import tech.msop.core.context.MsHttpHeadersGetter;
import tech.msop.core.context.MsServletContext;
import tech.msop.core.context.ServletHttpHeadersGetter;
import tech.msop.core.context.properties.MsContextProperties;
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
@AutoConfiguration
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
