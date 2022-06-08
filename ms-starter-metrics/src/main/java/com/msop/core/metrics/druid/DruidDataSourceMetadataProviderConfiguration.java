package com.msop.core.metrics.druid;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.jdbc.DataSourceUnwrapper;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({DruidDataSource.class})
public class DruidDataSourceMetadataProviderConfiguration {

    @Bean
    public DataSourcePoolMetadataProvider druidDataSourceMetadataProvider(){
        return (dataSource) -> {
            DruidDataSource druidDataSource =(DruidDataSource) DataSourceUnwrapper.unwrap(dataSource,DruidDataSource.class);
            return druidDataSource != null ? new DruidDataSourcePoolMetadata(druidDataSource) : null;
        };
    }
}
