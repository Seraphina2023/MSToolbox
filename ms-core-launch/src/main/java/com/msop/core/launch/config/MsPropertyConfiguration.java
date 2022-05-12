package com.msop.core.launch.config;


import com.msop.core.launch.properties.MsProperties;
import com.msop.core.launch.properties.MsPropertySourcePostProcessor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
@EnableConfigurationProperties(MsProperties.class)
public class MsPropertyConfiguration {

    @Bean
    public MsPropertySourcePostProcessor msPropertySourcePostProcessor(){
        return new MsPropertySourcePostProcessor();
    }
}
