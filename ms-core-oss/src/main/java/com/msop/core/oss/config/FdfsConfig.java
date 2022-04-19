package com.msop.core.oss.config;

import com.github.tobato.fastdfs.service.DefaultFastFileStorageClient;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.msop.core.oss.propertis.OssProperties;
import com.msop.core.oss.template.FdfsTemplate;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * FastDFS配置
 * @author ruozhuliufeng
 */
@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@EnableConfigurationProperties(OssProperties.class)
@ConditionalOnProperty(value = "oss.name", havingValue = "fastdfs")
public class FdfsConfig {
    private OssProperties ossProperties;
    private FastFileStorageClient fastFileStorageClient;

    @Bean
    @ConditionalOnBean(FastFileStorageClient.class)
    public FastFileStorageClient fastFileStorageClient(){
        return new DefaultFastFileStorageClient();
    }

    @Bean
    @ConditionalOnMissingBean(FdfsTemplate.class)
    @ConditionalOnBean(FastFileStorageClient.class)
    public FdfsTemplate fdfsTemplate(){
        return new FdfsTemplate(ossProperties,fastFileStorageClient);
    }
}
