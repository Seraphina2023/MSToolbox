package com.msop.core.boot.config;

import com.msop.core.boot.properties.MsFileProperties;
import com.msop.core.boot.properties.MsUploadProperties;
import com.msop.core.boot.resolver.TokenArgumentResolver;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.List;

/**
 * Web 配置
 *
 * @author ruozhulifeng
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@Order(Ordered.HIGHEST_PRECEDENCE)
@AllArgsConstructor
@EnableConfigurationProperties({
        MsFileProperties.class, MsUploadProperties.class
})
public class MsWebMvcConfiguration implements WebMvcConfigurer {
    private final MsUploadProperties msUploadProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String path = msUploadProperties.getSavePath();
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:" + path + "/upload/");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new TokenArgumentResolver());
    }
}
