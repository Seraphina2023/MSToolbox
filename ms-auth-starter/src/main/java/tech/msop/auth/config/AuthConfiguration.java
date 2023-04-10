package tech.msop.auth.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import tech.msop.auth.properties.MsSecurityProperties;
import tech.msop.auth.properties.TokenStoreProperties;

@EnableConfigurationProperties({MsSecurityProperties.class, TokenStoreProperties.class})
@AutoConfiguration
public class AuthConfiguration {
}
