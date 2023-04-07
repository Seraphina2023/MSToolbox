package tech.msop.auth.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import tech.msop.auth.properties.MsSecurityProperties;
import tech.msop.core.token.properties.MsTokenProperties;

@EnableConfigurationProperties({MsTokenProperties.class, MsSecurityProperties.class})
@AutoConfiguration
public class AuthAutoConfiguration {
}
