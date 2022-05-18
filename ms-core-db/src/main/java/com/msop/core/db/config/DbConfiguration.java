package com.msop.core.db.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.msop.core.launch.properties.MsPropertySource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 数据源配置类
 *
 * @author ruozhuliufeng
 */
@Configuration
@MsPropertySource(value = "classpath:/ms-db.yml")
public class DbConfiguration {

    /**
     * mybatis-plus自3.4.0起采用新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     */
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> configuration.setUseDeprecatedExecutor(false);
    }
}
