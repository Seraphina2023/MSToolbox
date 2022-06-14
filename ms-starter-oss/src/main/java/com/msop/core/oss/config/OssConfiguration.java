package com.msop.core.oss.config;

import com.msop.core.oss.propertis.OssProperties;
import com.msop.core.oss.rule.MsOssRule;
import com.msop.core.oss.rule.OssRule;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Oss 配置类
 *
 * @author ruozhuliufeng
 */
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(OssProperties.class)
public class OssConfiguration {

    private final OssProperties ossProperties;

    @Bean
    @ConditionalOnMissingBean(OssRule.class)
    public OssRule ossRule() {
        return new MsOssRule(ossProperties.getTenantMode());
    }
}
