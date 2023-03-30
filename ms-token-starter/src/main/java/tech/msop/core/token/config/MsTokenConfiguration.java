package tech.msop.core.token.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import tech.msop.core.token.properties.MsTokenProperties;

/**
 * Token 配置类
 */
@AutoConfiguration
@EnableConfigurationProperties(MsTokenProperties.class)
public class MsTokenConfiguration {
}
