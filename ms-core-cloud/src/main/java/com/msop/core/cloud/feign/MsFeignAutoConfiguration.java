package com.msop.core.cloud.feign;

import feign.Contract;
import feign.Feign;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.MsFeignClientsRegistrar;
import org.springframework.cloud.openfeign.MsHystrixTargeter;
import org.springframework.cloud.openfeign.Targeter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.ConversionService;

/**
 * MS feign 增强配置
 *
 * @author ruozhuliufeng
 */
@Configuration
@ConditionalOnClass(Feign.class)
@Import(MsFeignClientsRegistrar.class)
@AutoConfigureAfter(EnableFeignClients.class)
public class MsFeignAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(Targeter.class)
    public MsHystrixTargeter msFeignTargeter(){
        return new MsHystrixTargeter();
    }
}
