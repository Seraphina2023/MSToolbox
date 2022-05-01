package com.msop.core.secure;

import com.msop.core.secure.properties.SecurityProperties;
import com.msop.core.secure.properties.TokenStoreProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * 鉴权自动配置
 *
 * @author ruozhuliufeng
 */
@EnableConfigurationProperties({SecurityProperties.class, TokenStoreProperties.class})
@ComponentScan
public class AuthClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SecurityProperties securityProperties() {
        return new SecurityProperties();
    }
}
