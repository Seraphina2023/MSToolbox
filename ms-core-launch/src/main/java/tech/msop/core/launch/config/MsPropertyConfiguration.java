package tech.msop.core.launch.config;


import org.springframework.boot.autoconfigure.AutoConfiguration;
import tech.msop.core.launch.properties.MsProperties;
import tech.msop.core.launch.properties.MsPropertySourcePostProcessor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@AutoConfiguration
@Order(Ordered.HIGHEST_PRECEDENCE)
@EnableConfigurationProperties(MsProperties.class)
public class MsPropertyConfiguration {

    @Bean
    public MsPropertySourcePostProcessor msPropertySourcePostProcessor(){
        return new MsPropertySourcePostProcessor();
    }
}
