package com.msop.core.cloud.version;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * url版本号处理<br>
 * 参考：<a href="https://gitee.com/lianqu1990/spring-boot-starter-version-mapping">Github链接</a>
 *
 * @author ruozhuliufeng
 */
@Configuration
@ConditionalOnWebApplication
public class VersionMappingAutoConfiguration {
    @Bean
    public WebMvcRegistrations msWebMvcRegistrations() {
        return new MsWebMvcRegistrations();
    }
}
