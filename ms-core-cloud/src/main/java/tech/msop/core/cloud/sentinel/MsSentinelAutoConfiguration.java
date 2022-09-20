package tech.msop.core.cloud.sentinel;

import com.alibaba.cloud.sentinel.feign.SentinelFeignAutoConfiguration;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import tech.msop.core.cloud.feign.MsFeignRequestInterceptor;
import feign.Feign;
import feign.RequestInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

/**
 * Sentinel 配置类
 *
 * @author ruozhuliufeng
 */
@AllArgsConstructor
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(SentinelFeignAutoConfiguration.class)
@ConditionalOnProperty(name = "feign.sentinel.enabled")
public class MsSentinelAutoConfiguration {

    @Bean
    @Primary
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Feign.Builder feignSentinelBuilder(RequestInterceptor requestInterceptor) {
        return MsFeignSentinel.builder().requestInterceptor(requestInterceptor);
    }

    @Bean
    @ConditionalOnMissingBean
    public RequestInterceptor requestInterceptor() {
        return new MsFeignRequestInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean
    public BlockExceptionHandler blockExceptionHandler() {
        return new MsBlockExceptionHandler();
    }


}
