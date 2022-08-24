package com.msop.core.datascope.config;

import com.msop.core.datascope.handler.DataScopeHandler;
import com.msop.core.datascope.handler.MsDataScopeHandler;
import com.msop.core.datascope.handler.MsScopeModelHandler;
import com.msop.core.datascope.handler.ScopeModelHandler;
import com.msop.core.datascope.interceptor.DataScopeInterceptor;
import com.msop.core.datascope.properties.DataScopeProperties;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 数据权限配置类
 *
 * @author ruozhuliufeng
 */
@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@EnableConfigurationProperties(DataScopeProperties.class)
public class DataScopeConfiguration {
    private final JdbcTemplate jdbcTemplate;

    @Bean
    @ConditionalOnMissingBean(ScopeModelHandler.class)
    public ScopeModelHandler scopeModelHandler() {
        return new MsScopeModelHandler(jdbcTemplate);
    }

    @Bean
    @ConditionalOnBean(ScopeModelHandler.class)
    @ConditionalOnMissingBean(DataScopeHandler.class)
    public DataScopeHandler dataScopeHandler(ScopeModelHandler scopeModelHandler) {
        return new MsDataScopeHandler(scopeModelHandler);
    }

    @Bean
    @ConditionalOnBean(DataScopeHandler.class)
    @ConditionalOnMissingBean(DataScopeInterceptor.class)
    public DataScopeInterceptor interceptor(DataScopeHandler dataScopeHandler, DataScopeProperties dataScopeProperties) {
        return new DataScopeInterceptor(dataScopeHandler, dataScopeProperties);
    }
}
